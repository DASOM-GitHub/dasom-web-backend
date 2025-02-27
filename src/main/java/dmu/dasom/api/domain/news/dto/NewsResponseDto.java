package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(name = "NewsResponseDto", description = "뉴스 응답 DTO")
public class NewsResponseDto {

    @Schema(description = "소식 ID", example = "1")
    private Long id;

    @Schema(description = "뉴스 제목", example = "제목")
    private String title;

    @Schema(description = "뉴스 내용", example = "내용")
    private String content;

    @Schema(description = "작성일", example = "2025-02-14T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Base64 인코딩된 이미지", example = "[data:image/jpeg;base64,xxxxxx]", nullable = true)
    private List<String> imageUrls;

    public NewsResponseDto(Long id, String title, String content, LocalDateTime createdAt, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
    }

}