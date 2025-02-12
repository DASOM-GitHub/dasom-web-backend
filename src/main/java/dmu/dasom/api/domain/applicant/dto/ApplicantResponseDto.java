package dmu.dasom.api.domain.applicant.dto;

import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "ApplicantResponseDto", description = "지원자 응답 DTO")
public class ApplicantResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "학번", example = "20210000")
    private String studentNo;

    @Schema(description = "상태", example = "PENDING")
    private ApplicantStatus status;

}
