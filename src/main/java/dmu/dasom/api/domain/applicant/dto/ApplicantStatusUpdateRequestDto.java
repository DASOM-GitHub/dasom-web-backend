package dmu.dasom.api.domain.applicant.dto;

import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "ApplicantStatusUpdateRequestDto", description = "지원자 상태 변경 요청 DTO")
public class ApplicantStatusUpdateRequestDto {

    @Schema(description = "상태", example = "DOCUMENT_PASSED")
    private ApplicantStatus status;

}
