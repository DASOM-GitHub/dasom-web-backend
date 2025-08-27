package dmu.dasom.api.domain.executive.dto;

import dmu.dasom.api.domain.executive.entity.ExecutiveEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ExecutiveRequestDto", description = "임원진 요청 DTO")
public class ExecutiveRequestDto {

    private Long id;

    @NotBlank(message = "임원진 이름은 필수 입력 사항입니다.")
    @Size(max = 50, message = "임원진 이름은 최대 50자입니다.")
    @Schema(description = "임원진 이름", example = "김다솜")
    private String name;

    @NotBlank(message = "임원진 역할은 필수 입력 사항입니다.")
    @Schema(description = "임원진 역할", example = "회장")
    private String position;

    private String githubUrl;

    public ExecutiveEntity toEntity() {
        return ExecutiveEntity.builder()
                .name(this.name)
                .position(this.position)
                .githubUrl(this.githubUrl)
                .build();
    }
}
