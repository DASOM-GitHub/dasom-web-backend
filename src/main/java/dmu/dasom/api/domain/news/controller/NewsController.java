package dmu.dasom.api.domain.news.controller;

import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.service.NewsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "NEWS API", description = "다솜소식 API")
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 전체 조회
    @Operation(summary = "소식 조회", description = "리스트로 조회")
    @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> getAllNews() {
        List<NewsResponseDto> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    // 개별 조회
    @Operation(summary = "소식 상세 조회", description = "ID로 특정 소식을 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "400", description = "소식을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {
        NewsResponseDto responseDto = newsService.getNewsById(id);
        return ResponseEntity.ok(responseDto);
    }

    // 생성 (multipart/form-data 사용)
    @Operation(summary = "소식 등록", description = "새로운 소식을 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsResponseDto> createNews(
            @Valid @RequestPart("news") NewsRequestDto requestDto, // JSON 데이터 받기
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles // 이미지 파일 받기
    ) {
        NewsResponseDto responseDto = newsService.createNews(requestDto, imageFiles);
        return ResponseEntity.status(201).body(responseDto);
    }

    // 수정 (multipart/form-data 사용)
    @Operation(summary = "소식 수정", description = "ID로 특정 소식을 수정")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewsResponseDto> updateNews(
            @PathVariable Long id,
            @Valid @RequestPart("news") NewsRequestDto requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageFiles
    ) {
        NewsResponseDto updatedNews = newsService.updateNews(id, requestDto, imageFiles);
        return ResponseEntity.ok(updatedNews);
    }

    // 삭제
    @Operation(summary = "소식 삭제", description = "ID로 특정 소식을 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "소식을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

}