package dmu.dasom.api.global.file.controller;

import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.global.file.dto.FileResponseDto;
import dmu.dasom.api.global.file.enums.FileType;
import dmu.dasom.api.global.file.service.FileService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파일 업로드 성공"),
        @ApiResponse(responseCode = "400", description = "파일 업로드 실패",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "파일 업로드 실패",
                        value = "{ \"code\": \"C028\", \"message\": \"파일 인코딩에 실패하였습니다.\" }")
                }
            ))
    })
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> uploadFiles(
        @RequestParam("files") List<MultipartFile> files,
        @RequestParam("fileType") FileType fileType,
        @RequestParam("targetId") @Min(1) Long targetId
    ) {
        fileService.uploadFiles(files, fileType, targetId);
        return ResponseEntity.ok()
            .build();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "파일 조회 성공"),
        @ApiResponse(responseCode = "400", description = "파일 조회 실패",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "파일 조회 실패",
                        value = "{ \"code\": \"C012\", \"message\": \"조회 결과가 없습니다.\" }")
                }
            ))
    })
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponseDto> getFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

}
