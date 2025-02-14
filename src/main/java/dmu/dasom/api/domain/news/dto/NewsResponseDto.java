package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "응답 DTO")
public class NewsResponseDto {

    @Schema(description = "소식 ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "제목")
    private String title;

    @Schema(description = "내용", example = "내용")
    private String content;

    @Schema(description = "작성일", example = "2025-02-14T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "이미지 URL", example = "http://example.com/image.jpg")
    private String imageUrl;

    public NewsResponseDto(Long id, String title, String content, LocalDateTime createdAt, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }
}
