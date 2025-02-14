package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "뉴스 요청 DTO")
public class NewsRequestDto {

    @Schema(description = "뉴스 제목", example = "새로운 뉴스 제목")
    @NotNull
    private String title;

    @Schema(description = "뉴스 내용", example = "새로운 뉴스 내용")
    @NotNull
    private String content;

    @Schema(description = "뉴스 이미지 URL", example = "http://example.com/new-image.jpg")
    private String imageUrl;

}