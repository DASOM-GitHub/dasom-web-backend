package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(name = "NewsRequestDto", description = "뉴스 생성 요청 DTO")
public class NewsRequestDto {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "뉴스 제목", example = "새로운 뉴스 제목")
    private String title;

    @NotBlank
    @Schema(description = "뉴스 내용", example = "새로운 뉴스 내용")
    private String content;

    @Size(max = 255)
    @Schema(description = "뉴스 이미지 URL", example = "http://example.com/image.jpg", nullable = true)
    private String imageUrl;
}