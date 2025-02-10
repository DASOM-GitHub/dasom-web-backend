package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;

public interface ApplicantService {

    void apply(final ApplicantCreateRequestDto request);

}
