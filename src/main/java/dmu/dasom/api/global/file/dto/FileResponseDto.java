package dmu.dasom.api.global.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FileResponseDto", description = "파일 응답 DTO")
public class FileResponseDto {

    @Schema(description = "파일 ID", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "파일 형식", example = "image/png")
    @NotNull
    private String fileFormat;

    @Schema(description = "인코딩 된 파일", example = "asdf")
    @NotNull
    private String encodedData;   // Base64 인코딩 데이터

}
