package dmu.dasom.api.domain.recruit.controller;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.service.RecruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private final ApplicantService applicantService;
    private final RecruitService recruitService;

    // 지원하기
    @Operation(summary = "부원 지원하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지원 성공"),
            @ApiResponse(responseCode = "400", description = "실패 케이스",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "학번 중복",
                                value = "{ \"code\": \"C013\", \"message\": \"이미 등록된 학번입니다.\" }")}))})
    @PostMapping("/apply")
    public ResponseEntity<Void> apply(@Valid @RequestBody final ApplicantCreateRequestDto request) {
        applicantService.apply(request);
        return ResponseEntity.ok().build();
    }

    // 모집 일정 조회
    @Operation(summary = "모집 일정 조회")
    @ApiResponse(responseCode = "200", description = "모집 일정 조회 성공")
    @GetMapping
    public ResponseEntity<List<RecruitConfigResponseDto>> getRecruitSchedule() {
        return ResponseEntity.ok(recruitService.getRecruitSchedule());
    }

}
