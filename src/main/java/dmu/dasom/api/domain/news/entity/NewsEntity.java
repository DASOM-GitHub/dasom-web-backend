package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.common.Status;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "news_images", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "image_url", length = 255)
    @Schema(description = "뉴스 이미지 URL 리스트", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
    private List<String> imageUrls;

    // 뉴스 상태 업데이트
    public void updateStatus(Status status) {
        super.updateStatus(status);
    }

    // NewsEntity → NewsResponseDto 변환
    public NewsResponseDto toResponseDto() {
        return new NewsResponseDto(id, title, content, getCreatedAt(), imageUrls);
    }

    // 수정 메서드
    public void update(String title, String content, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }

}