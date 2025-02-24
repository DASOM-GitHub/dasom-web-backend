package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.common.Status;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Entity
@Table(name = "news")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "뉴스 엔티티")
public class NewsEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "뉴스 ID", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "뉴스 제목", example = "뉴스 예제 제목")
    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @NotNull
    @Schema(description = "뉴스 내용", example = "뉴스 예제 내용")
    @Column(nullable = false)
    private String content;


    @Schema(description = "뉴스 이미지 URL", example = "http://example.com/image.jpg")
    @Column(length = 255)
    private String imageUrl;

    // 뉴스 상태 업데이트
    public void updateStatus(Status status) {
        super.updateStatus(status);
    }

    // NewsEntity → NewsResponseDto 변환
    public NewsResponseDto toResponseDto() {
        return new NewsResponseDto(id, title, content, getCreatedAt(), imageUrl);
    }

    //수정기능
    public void update(String title, String content, String imageUrl) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자까지");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다");
        }
        if (imageUrl != null && imageUrl.length() > 255) {
            throw new IllegalArgumentException("이미지 URL은 255자까지");
        }

        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

}