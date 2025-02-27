package dmu.dasom.api.global.file.controller;

import dmu.dasom.api.global.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/global/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<List<Long>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<Long> fileIds = fileService.uploadFiles(files);
        return ResponseEntity.ok(fileIds);
    }

    // 파일 조회
    @GetMapping("/{fileId}")
    public ResponseEntity<String> getFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.getFileBase64(fileId));
    }

}