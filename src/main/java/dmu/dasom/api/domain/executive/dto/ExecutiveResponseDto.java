package dmu.dasom.api.domain.executive.dto;

import dmu.dasom.api.domain.executive.enums.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ExecutiveResponseDto", description = "임원진 응답 DTO")
public class ExecutiveResponseDto {

    @Schema(description = "임원진 ID", example = "1")
    private Long id;

    @Schema(description = "임원진 이름", example = "김다솜")
    private String name;

    @Schema(description = "임원진 직책", example = "회장")
    private String position;

    @Schema(description = "수정할 임원진 역할", example = "동아리 운영 총괄")
    private String role;

    @Schema(description = "임원진 깃허브 이름", example = "DASOM")
    private String github_username;

    @Schema(description = "소속 팀", example = "president, tech, academic, pr, management")
    private Team team;

    private Integer sortOrder;
}
