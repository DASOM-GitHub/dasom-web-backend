package dmu.dasom.api.domain.activity.controller;

import dmu.dasom.api.domain.activity.dto.ActivityRequestDto;
import dmu.dasom.api.domain.activity.service.ActivityService;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
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
@RequestMapping("/api/admin/activities")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Activity API", description = "어드민 활동 연혁 관리 API")
public class AdminActivityController {

    private final ActivityService activityService;

    @Operation(summary = "활동 연혁 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "활동 연혁 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 유효성 검사 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "유효성 검사 실패", value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }"))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "인증 실패", value = "{ \"code\": \"C001\", \"message\": \"인증되지 않은 사용자입니다.\" }"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ADMIN이 아님)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "권한 없음", value = "{ \"code\": \"C002\", \"message\": \"접근 권한이 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "서버 문제 발생", value = "{ \"code\": \"C009\", \"message\": \"서버에 문제가 발생하였습니다.\" }")))
    })
    @PostMapping
    public ResponseEntity<Void> createActivity(@Valid @RequestBody ActivityRequestDto requestDto) {
        activityService.createActivity(requestDto);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "활동 연혁 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "활동 연혁 수정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 값 유효성 검사 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "유효성 검사 실패", value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }"))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "인증 실패", value = "{ \"code\": \"C001\", \"message\": \"인증되지 않은 사용자입니다.\" }"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ADMIN이 아님)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "권한 없음", value = "{ \"code\": \"C002\", \"message\": \"접근 권한이 없습니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "수정할 리소스를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "조회 결과 없음", value = "{ \"code\": \"C010\", \"message\": \"해당 리소스를 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "서버 문제 발생", value = "{ \"code\": \"C009\", \"message\": \"서버에 문제가 발생하였습니다.\" }")))
    })
    @PutMapping("/{activityId}")
    public ResponseEntity<Void> updateActivity(
            @PathVariable @Min(1) Long activityId,
            @Valid @RequestBody ActivityRequestDto requestDto
    ) {
        activityService.updateActivity(activityId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "활동 연혁 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "활동 연혁 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "인증 실패", value = "{ \"code\": \"C001\", \"message\": \"인증되지 않은 사용자입니다.\" }"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (ADMIN이 아님)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "권한 없음", value = "{ \"code\": \"C002\", \"message\": \"접근 권한이 없습니다.\" }"))),
            @ApiResponse(responseCode = "404", description = "삭제할 리소스를 찾을 수 없음", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "조회 결과 없음", value = "{ \"code\": \"C010\", \"message\": \"해당 리소스를 찾을 수 없습니다.\" }"))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "서버 문제 발생", value = "{ \"code\": \"C009\", \"message\": \"서버에 문제가 발생하였습니다.\" }")))
    })
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
