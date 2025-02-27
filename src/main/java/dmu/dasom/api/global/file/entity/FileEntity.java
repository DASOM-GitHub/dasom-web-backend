package dmu.dasom.api.global.file.entity;

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

}