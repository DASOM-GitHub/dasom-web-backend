package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(name = "NewsResponseDto", description = "뉴스 응답 DTO")
public class NewsResponseDto {

    @Schema(description = "소식 ID", example = "1")
    private final Long id;

    @Schema(description = "뉴스 제목", example = "제목")
    private final String title;

    @Schema(description = "뉴스 내용", example = "내용")
    private final String content;

    @Schema(description = "작성일", example = "2025-02-14T12:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "뉴스 이미지 URL", example = "http://example.com/image.jpg", nullable = true)
    private final String imageUrl;

    public NewsResponseDto(Long id, String title, String content, LocalDateTime createdAt, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }
}