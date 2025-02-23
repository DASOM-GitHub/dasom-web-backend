package dmu.dasom.api.domain.applicant.service;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantStatusUpdateRequestDto;
import dmu.dasom.api.domain.email.enums.MailType;
import dmu.dasom.api.global.dto.PageResponse;

public interface ApplicantService {

    void apply(final ApplicantCreateRequestDto request);

    PageResponse<ApplicantResponseDto> getApplicants(final int page);

    ApplicantDetailsResponseDto getApplicant(final Long id);

    ApplicantDetailsResponseDto updateApplicantStatus(final Long id, final ApplicantStatusUpdateRequestDto request);

    void sendEmailsToApplicants(MailType mailType);

    ApplicantDetailsResponseDto getApplicantByStudentNo(final String studentNo);

}
