package dmu.dasom.api.global.admin.controller;

import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.news.dto.NewsCreationResponseDto;
import dmu.dasom.api.domain.news.dto.NewsRequestDto;
import dmu.dasom.api.domain.news.dto.NewsResponseDto;
import dmu.dasom.api.domain.news.dto.NewsUpdateRequestDto;
import dmu.dasom.api.domain.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/news")
@RequiredArgsConstructor
@Tag(name = "ADMIN - News API", description = "어드민 소식 관리 API")
public class AdminNewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 등록")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "뉴스 등록 성공")
    })
    @PostMapping
    public ResponseEntity<NewsCreationResponseDto> createNews(@Valid @RequestBody NewsRequestDto requestDto) {
        return ResponseEntity.status(201)
            .body(newsService.createNews(requestDto));
    }

    @Operation(summary = "뉴스 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "뉴스 수정 성공"),
        @ApiResponse(responseCode = "404", description = "조회 결과 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "조회 결과 없음",
                        value = "{ \"code\": \"C010\", \"message\": \"해당 리소스를 찾을 수 없습니다.\" }"
                    )
                }
            ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(
        @PathVariable @Min(1) Long id,
        @Valid @RequestBody NewsUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(newsService.updateNews(id, requestDto));
    }

    @Operation(summary = "뉴스 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "뉴스 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "조회 결과 없음",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "조회 결과 없음",
                        value = "{ \"code\": \"C010\", \"message\": \"해당 리소스를 찾을 수 없습니다.\" }"
                    )
                }
            ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok()
            .build();
    }

}
