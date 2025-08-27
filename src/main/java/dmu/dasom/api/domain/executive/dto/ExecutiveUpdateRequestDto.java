package dmu.dasom.api.domain.executive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecutiveUpdateRequestDto", description = "임원진 멤버 수정 요청 DTO")
public class ExecutiveUpdateRequestDto {

    @Size(max = 50, message = "임원진 이름은 최대 50자입니다.")
    @Schema(description = "수정할 임원진 이름", example = "김다솜", nullable = true)
    private String name;

    @Schema(description = "수정할 임원진 직책", example = "회장", nullable = true)
    private String position;

    @Schema(description = "수정할 임원진 깃허브 주소", example = "https://github.com/dasom", nullable = true)
    private String githubUrl;
}
