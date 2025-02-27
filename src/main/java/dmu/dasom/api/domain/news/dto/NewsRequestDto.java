package dmu.dasom.api.domain.news.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NewsRequestDto", description = "뉴스 생성 요청 DTO")
public class NewsRequestDto {

    @NotBlank(message = "뉴스 제목은 필수입니다.")
    @Size(max = 100, message = "뉴스 제목은 최대 100자입니다.")
    @Schema(description = "뉴스 제목", example = "새로운 뉴스 제목")
    private String title;

    @NotBlank(message = "뉴스 내용은 필수입니다.")
    @Schema(description = "뉴스 내용", example = "새로운 뉴스 내용")
    private String content;

    @Schema(description = "이미지 파일 ID 목록", example = "[1, 2, 3]", nullable = true)
    private List<Long> fileIds;
}