package dmu.dasom.api.global.file.controller;

import dmu.dasom.api.global.file.dto.FileResponseDto;
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

    @PostMapping("/upload")
    public ResponseEntity<List<Long>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<Long> fileIds = fileService.uploadFiles(files);
        return ResponseEntity.ok(fileIds);
    }

    // dto 반환으로 수정
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponseDto> getFile(@PathVariable Long fileId) {
        return ResponseEntity.ok(fileService.getFileById(fileId));
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponseDto>> getFiles(@RequestParam List<Long> fileIds) {
        return ResponseEntity.ok(fileService.getFilesByIds(fileIds));
    }

}