package dmu.dasom.api.domain.recruit.dto;

import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ParameterObject
@Schema(name = "ResultCheckRequestDto", description = "지원 결과 확인 요청 DTO")
public class ResultCheckRequestDto {

    @NotNull
    @Parameter(description = "지원 결과 조회 타입")
    @Schema(example = "DOCUMENT_PASS")
    private ResultCheckType type;

    @NotNull
    @Parameter(description = "학번")
    @Schema(example = "20210000")
    private String studentNo;

    @NotNull
    @Parameter(description = "전화번호 뒷자리")
    @Schema(example = "6789")
    private String contactLastDigit;

}
