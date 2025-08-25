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

    @Schema(description = "파일 URL", example = "url")
    @NotNull
    private String encodedData;   // r2에 저장된 파일의 URL

    @Schema(description = "파일 형식", example = "image/png")
    @NotNull
    private String fileFormat;
}
