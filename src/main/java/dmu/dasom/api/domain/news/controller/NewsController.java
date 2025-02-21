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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "NEWS API", description = "다솜소식 API")
@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "소식 조회", description = "리스트로 조회")
    @ApiResponse(responseCode = "200", description = "정상 응답",
            content = @Content(mediaType = "application/json"))
    @GetMapping
    public ResponseEntity<List<NewsResponseDto>> getAllNews() {
        List<NewsResponseDto> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

    @Operation(summary = "소식 등록", description = "새로운 소식을 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 완료"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"errorCode\": \"E003\", \"errorMessage\": \"유효하지 않은 입력입니다.\" }")))
    })

    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto requestDto) {
        NewsResponseDto responseDto = newsService.createNews(requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

}