package dmu.dasom.api.global.file.entity;

import dmu.dasom.api.domain.news.entity.NewsEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName;

    @Lob
    @Column(name = "base64data", nullable = false, columnDefinition = "CLOB")
    private String base64Data;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private NewsEntity news;

    public FileEntity(Long id, String originalName, String base64Data, String fileType, Long fileSize) {
        this.id = id;
        this.originalName = originalName;
        this.base64Data = base64Data;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }
}