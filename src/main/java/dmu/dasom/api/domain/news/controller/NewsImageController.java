package dmu.dasom.api.domain.news.controller;

import dmu.dasom.api.domain.news.service.NewsImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/news/images")
@RequiredArgsConstructor
public class NewsImageController {

    private final NewsImageService newsImageService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> imageUrls = newsImageService.uploadImages(images);
            return ResponseEntity.ok(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of("파일 업로드 실패: " + e.getMessage()));
        }
    }

}