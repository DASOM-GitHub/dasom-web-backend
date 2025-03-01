package dmu.dasom.api.domain.news.dto;

import dmu.dasom.api.global.file.dto.FileResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "NewsListResponseDto", description = "뉴스 리스트 응답 DTO")
public class NewsListResponseDto {

    @Schema(description = "소식 ID", example = "1")
    private Long id;

    @Schema(description = "뉴스 제목", example = "제목")
    private String title;

    @Schema(description = "작성일", example = "2025-02-14T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "인코딩된 이미지", example = "asdf", nullable = true)
    private FileResponseDto image;

}
