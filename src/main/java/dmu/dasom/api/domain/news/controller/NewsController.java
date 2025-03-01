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

@Tag(name = "NEWS API", description = "뉴스 API")
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

    @Operation(summary = "뉴스 등록")
    @PostMapping
    public ResponseEntity<NewsCreationResponseDto> createNews(@Valid @RequestBody NewsRequestDto requestDto) {
        return ResponseEntity.status(201).body(newsService.createNews(requestDto));
    }

    @Operation(summary = "뉴스 수정")
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable @Min(1) Long id,
                                                      @Valid @RequestBody NewsUpdateRequestDto requestDto) {
        return ResponseEntity.ok(newsService.updateNews(id, requestDto));
    }

    @Operation(summary = "뉴스 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

}
