package dmu.dasom.api.domain.news.controller;

import dmu.dasom.api.domain.news.dto.*;
import dmu.dasom.api.domain.news.service.NewsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "News API", description = "뉴스 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "전체 뉴스 조회 (썸네일 포함)")
    @GetMapping
    public ResponseEntity<List<NewsListResponseDto>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @Operation(summary = "뉴스 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable @Min(1) Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

}
