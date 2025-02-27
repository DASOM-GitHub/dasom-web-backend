package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
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
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ElementCollection
    @CollectionTable(name = "news_images", joinColumns = @JoinColumn(name = "news_id"))
    @Column(name = "image_data", columnDefinition = "TEXT")
    private List<String> imageUrls;

    public void update(String title, String content, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
    }

    public NewsResponseDto toResponseDto() {
        return new NewsResponseDto(id, title, content, getCreatedAt(), imageUrls);
    }

}