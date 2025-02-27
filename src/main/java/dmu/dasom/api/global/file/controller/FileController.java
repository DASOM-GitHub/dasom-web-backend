package dmu.dasom.api.global.file.controller;

import dmu.dasom.api.global.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File API", description = "파일 업로드 API")
@RestController
@RequestMapping("/api/global/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 업로드", description = "여러 개의 파일 업로드 + 파일 ID 리스트를 반환")
    @PostMapping("/upload")
    public ResponseEntity<List<Long>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<Long> fileIds = fileService.uploadFiles(files);
        return ResponseEntity.ok(fileIds);
    }

}