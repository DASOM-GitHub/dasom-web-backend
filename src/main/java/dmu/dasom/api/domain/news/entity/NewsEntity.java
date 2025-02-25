package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.common.Status;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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

    @Column(nullable = false, length = 100)
    @Schema(description = "뉴스 제목", example = "뉴스 예제 제목")
    private String title;

    @Lob
    @Column(nullable = false)
    @Schema(description = "뉴스 내용", example = "뉴스 예제 내용")
    private String content;

    @Column(length = 255)
    @Schema(description = "뉴스 이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    // 뉴스 상태 업데이트
    public void updateStatus(Status status) {
        super.updateStatus(status);
    }

    // NewsEntity → NewsResponseDto 변환
    public NewsResponseDto toResponseDto() {
        return new NewsResponseDto(id, title, content, getCreatedAt(), imageUrl);
    }

    // 수정
    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

}