package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NewsUpdateRequestDto", description = "뉴스 수정 요청 DTO")
public class NewsUpdateRequestDto {

    @Size(max = 100, message = "뉴스 제목은 최대 100자입니다.")
    @Schema(description = "수정할 뉴스 제목", example = "뉴스 제목", nullable = true)
    private String title;

    @Schema(description = "수정할 뉴스 내용", example = "뉴스 내용", nullable = true)
    private String content;

    @Schema(description = "삭제할 이미지 ID 목록", example = "[1, 2, 3]", nullable = true)
    private List<Long> deleteImageIds;

}
