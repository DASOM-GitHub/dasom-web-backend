package dmu.dasom.api.domain.executive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "executiveCreationResponseDto", description = "회장단 생성 응답 DTO")
public class executiveCreationResponseDto {

    @NotNull
    @Schema(description = "회장단 ID", example = "1")
    private Long id;

}
