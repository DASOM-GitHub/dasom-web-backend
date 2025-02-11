package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.global.dto.PageResponse;

public interface ApplicantService {

    void apply(final ApplicantCreateRequestDto request);

    PageResponse<ApplicantResponseDto> getApplicants(final int page);

}
