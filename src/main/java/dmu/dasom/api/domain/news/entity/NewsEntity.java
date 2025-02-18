package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.common.Status;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Entity
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

    @NotNull
    @Schema(description = "뉴스 내용", example = "뉴스 예제 내용")
    @Column(columnDefinition = "TEXT", nullable = false)
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
}