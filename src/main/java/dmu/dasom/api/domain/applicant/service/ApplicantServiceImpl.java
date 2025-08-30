package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantStatusUpdateRequestDto;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.google.enums.MailType;
import dmu.dasom.api.domain.google.service.EmailService;
import dmu.dasom.api.domain.google.service.GoogleApiService;
import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import dmu.dasom.api.domain.recruit.service.RecruitService;
import dmu.dasom.api.global.dto.PageResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final static int DEFAULT_PAGE_SIZE = 20;

    private final ApplicantRepository applicantRepository;
    private final EmailService emailService;
    private final GoogleApiService googleApiService;
    private final RecruitService recruitService;

    @Value("${google.spreadsheet.id}")
    private String spreadSheetId;

    // 지원자 저장
    @Override
    public void apply(final ApplicantCreateRequestDto request) {
        if (!recruitService.isRecruitmentActive())
            throw new CustomException(ErrorCode.RECRUITMENT_NOT_ACTIVE);

        final Optional<Applicant> applicant = findByStudentNo(request.getStudentNo());

        // 이미 지원한 학번이 존재할 경우
        if (applicant.isPresent()) {
            // 덮어쓰기 확인 여부가 false일 경우 예외 발생
            if (!request.getIsOverwriteConfirmed())
                throw new CustomException(ErrorCode.DUPLICATED_STUDENT_NO);

            Applicant existingApplicant = applicant.get();
            existingApplicant.overwrite(request);

            googleApiService.updateSheet(List.of(existingApplicant));
            return;
        }

        Applicant savedApplicant = applicantRepository.save(request.toEntity());
        googleApiService.appendToSheet(List.of(savedApplicant));
    }

    // 지원자 조회
    @Override
    public PageResponse<ApplicantResponseDto> getApplicants(final int page) {
        final Page<Applicant> applicants = applicantRepository.findAllWithPageRequest(PageRequest.of(page, DEFAULT_PAGE_SIZE));

        // 조회 조건에 따라 결과가 없을 수 있음
        if (applicants.isEmpty())
            throw new CustomException(ErrorCode.EMPTY_RESULT);

        return PageResponse.from(applicants.map(Applicant::toApplicantResponse));
    }

    // 지원자 상세 조회
    @Override
    public ApplicantDetailsResponseDto getApplicant(final Long id) {
        return findById(id).toApplicantDetailsResponse();
    }

    // 지원자 상태 변경
    @Override
    public ApplicantDetailsResponseDto updateApplicantStatus(final Long id, final ApplicantStatusUpdateRequestDto request) {
        final Applicant applicant = findById(id);
        // 지원자 상태 변경
        applicant.updateStatus(request.getStatus());

        // Google Sheets에서 학번(Student No)을 기준으로 사용자 존재 여부 확인
        int rowIndex = googleApiService.findRowIndexByStudentNo(spreadSheetId, "Sheet1", applicant.getStudentNo());
        if (rowIndex == -1) {
            // Google Sheets에 사용자 추가
            googleApiService.appendToSheet(List.of(applicant));
            log.info("지원자가 Google Sheets에 없어서 새로 추가되었습니다: {}", applicant.getStudentNo());
        } else {
            // Google Sheets에서 사용자 상태 업데이트
            googleApiService.updateSheet(List.of(applicant));
            log.info("Google Sheets에서 지원자 상태가 업데이트되었습니다: {}", applicant.getStudentNo());
        }

        return applicant.toApplicantDetailsResponse();
    }

    @Override
    public void sendEmailsToApplicants(MailType mailType) {
        List<Applicant> applicants;

        // MailType에 따라 지원자 조회
        switch (mailType) {
            case DOCUMENT_RESULT:
                applicants = applicantRepository.findAll();
                break;
            case FINAL_RESULT:
                applicants = applicantRepository.findByStatusIn(
                        List.of(ApplicantStatus.INTERVIEW_FAILED,
                                ApplicantStatus.INTERVIEW_PASSED)
                );
                break;
            default:
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        for (Applicant applicant : applicants) {
            emailService.sendEmail(applicant.getEmail(), applicant.getName(), mailType);
        }
    }

    // 학번으로 지원자 조회
    @Override
    public ApplicantDetailsResponseDto getApplicantByStudentNo(final String studentNo) {
        return findByStudentNo(studentNo)
            .map(Applicant::toApplicantDetailsResponse)
            .orElseThrow(() -> new CustomException(ErrorCode.ARGUMENT_NOT_VALID));
    }

    // 합격 결과 확인
    @Override
    public ResultCheckResponseDto checkResult(final ResultCheckRequestDto request) {
        // 결과 발표 시간 검증
        final LocalDateTime resultAnnouncementSchedule = recruitService.getResultAnnouncementSchedule(request.getType());

        // 설정 된 시간이 현재 시간보다 이전인 경우 예외 발생
        final LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(resultAnnouncementSchedule))
            throw new CustomException(ErrorCode.INVALID_INQUIRY_PERIOD);

        // 지원자 정보 조회
        final ApplicantDetailsResponseDto applicant = getApplicantByStudentNo(request.getStudentNo());

        // 연락처 뒷자리가 일치하지 않을 경우 예외 발생
        if (!applicant.getContact().split("-")[2].equals(request.getContactLastDigit()))
            throw new CustomException(ErrorCode.ARGUMENT_NOT_VALID);

        // 예약 코드 생성
        String reservationCode = recruitService.generateReservationCode(request.getStudentNo(), request.getContactLastDigit());

        // 합격 여부 반환
        return ResultCheckResponseDto.builder()
            .type(request.getType())
            .studentNo(applicant.getStudentNo())
            .name(applicant.getName())
            .reservationCode(reservationCode)
            .isPassed(request.getType().equals(ResultCheckType.DOCUMENT_PASS) ?
                applicant.getStatus()
                    .equals(ApplicantStatus.DOCUMENT_PASSED) :
                applicant.getStatus()
                    .equals(ApplicantStatus.INTERVIEW_PASSED))
            .build();
    }

    // Repository에서 ID로 지원자 조회
    private Applicant findById(final Long id) {
        return applicantRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RESULT));
    }

    // 학번으로 지원자 존재 여부 확인
    private Optional<Applicant> findByStudentNo(final String studentNo) {
        return applicantRepository.findByStudentNo(studentNo);
    }

}
