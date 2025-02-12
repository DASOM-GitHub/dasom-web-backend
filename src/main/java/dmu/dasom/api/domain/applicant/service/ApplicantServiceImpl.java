package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.global.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final static int DEFAULT_PAGE_SIZE = 20;

    private final ApplicantRepository applicantRepository;

    // 지원자 저장
    @Override
    public void apply(final ApplicantCreateRequestDto request) {
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
        return applicantRepository.findById(id)
                .map(Applicant::toApplicantDetailsResponse)
                .orElseThrow(() -> new CustomException(ErrorCode.EMPTY_RESULT));
    }

}
