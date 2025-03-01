package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NewsCreationResponseDto", description = "뉴스 생성 응답 DTO")
public class NewsCreationResponseDto {

    @NotNull
    @Schema(description = "뉴스 ID", example = "1")
    private Long id;

}
