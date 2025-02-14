package dmu.dasom.api.domain.news.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Schema(description = "뉴스 엔티티")
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "뉴스 ID", example = "1")
    private Long id;

    @Schema(description = "뉴스 제목", example = "뉴스 예제 제목")
    @NotNull
    private String title;

    @Schema(description = "뉴스 내용", example = "뉴스 예제 내용")
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @Schema(description = "뉴스 작성일", example = "2025-02-14T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "뉴스 이미지 URL", example = "http://example.com/image.jpg")
    private String imageUrl;
}
