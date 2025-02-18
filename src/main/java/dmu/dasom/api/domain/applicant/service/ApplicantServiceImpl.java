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
import dmu.dasom.api.domain.email.enums.MailType;
import dmu.dasom.api.domain.email.service.EmailService;
import dmu.dasom.api.global.dto.PageResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 지원자 저장
    @Override
    public void apply(final ApplicantCreateRequestDto request) {
        final Optional<Applicant> applicant = findByStudentNo(request.getStudentNo());

        // 이미 지원한 학번이 존재할 경우
        if (applicant.isPresent()) {
            // 덮어쓰기 확인 여부가 false일 경우 예외 발생
            if (!request.getIsOverwriteConfirmed())
                throw new CustomException(ErrorCode.DUPLICATED_STUDENT_NO);

            // 기존 지원자 정보 갱신 수행
            applicant.get().overwrite(request);
            return;
        }

        // 새로운 지원자일 경우 저장
        applicantRepository.save(request.toEntity());
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
        applicant.updateStatus(request.getStatus());

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
            try {
                emailService.sendEmail(applicant.getEmail(), applicant.getName(), mailType);
            } catch (MessagingException e) {
                System.err.println("Failed to send email to: " + applicant.getEmail());
            }
        }
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
