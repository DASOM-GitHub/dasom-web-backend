package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantStatusUpdateRequestDto;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.google.service.GoogleApiService;
import dmu.dasom.api.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ApplicantServiceImpl implements ApplicantService {

    private final static int DEFAULT_PAGE_SIZE = 20;

    private final ApplicantRepository applicantRepository;
    private final GoogleApiService googleApiService;

    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

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
        Applicant applicant = applicantRepository.save(request.toEntity());

        // 스프레드 시트에 기록할 데이터
        List<List<Object>> values = List.of(List.of(
                applicant.getId(),
                applicant.getStudentNo(),
                applicant.getContact(),
                applicant.getEmail(),
                applicant.getGrade(),
                applicant.getReasonForApply(),
                applicant.getActivityWish(),
                applicant.getIsPrivacyPolicyAgreed(),
                applicant.getCreatedAt(),
                applicant.getUpdatedAt()
        ));

        String range = "Sheet1!A:J";

        googleApiService.writeToSheet(spreadsheetId, range, values);
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
