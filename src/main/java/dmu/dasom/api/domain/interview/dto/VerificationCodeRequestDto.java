package dmu.dasom.api.domain.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VerificationCodeRequestDto {

    @NotNull(message = "학번은 필수 값입니다.")
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자로 구성되어야 합니다.")
    @Size(min = 8, max = 8)
    @Schema(description = "지원자 학번", example = "20250001")
    private String studentNo;
}
