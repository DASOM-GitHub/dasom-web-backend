package dmu.dasom.api.global.file.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String originalName;
    @Column(nullable = false)
    private String storedName;
    @Column(nullable = false)
    private String filePath;
    @Column(nullable = false)
    private String fileType;
    @Column(nullable = false)
    private Long fileSize;

}
