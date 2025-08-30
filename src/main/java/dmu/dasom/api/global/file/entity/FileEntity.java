package dmu.dasom.api.global.file.entity;

import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.enums.FileType;
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

    @Column(name = "ORIGINAL_FILE_NAME", nullable = false)
    private String originalName;

    @Lob
    @Column(name = "FILE_URL", nullable = false, columnDefinition = "CLOB")
    private String fileUrl;

    @Column(name = "FILE_FORMAT", nullable = false)
    private String fileFormat;

    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "FILE_TYPE", nullable = false)
    private FileType fileType;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;

    public FileResponseDto toResponseDto() {
        return FileResponseDto.builder()
            .id(id)
            .fileFormat(fileFormat)
            .encodedData(fileUrl)
            .build();
    }

}
