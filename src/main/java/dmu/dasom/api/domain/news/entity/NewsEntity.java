package dmu.dasom.api.domain.news.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.global.file.dto.FileResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

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

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NewsResponseDto toResponseDto(List<FileResponseDto> images) {
        return NewsResponseDto.builder()
            .id(this.id)
            .title(this.title)
            .content(this.content)
            .createdAt(getCreatedAt())
            .images(ObjectUtils.isEmpty(images) ? null : images)
            .build();
    }

    public NewsResponseDto toResponseDto() {
        return NewsResponseDto.builder()
            .id(this.id)
            .title(this.title)
            .content(this.content)
            .createdAt(getCreatedAt())
            .images(null)
            .build();
    }

}