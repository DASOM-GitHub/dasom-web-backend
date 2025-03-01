package dmu.dasom.api.domain.recruit.controller;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.interview.dto.InterviewReservationRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotCreateRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotResponseDto;
import dmu.dasom.api.domain.interview.service.InterviewService;
import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {

    private final ApplicantService applicantService;
    private final RecruitService recruitService;
    private final InterviewService interviewService;

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
                        value = "{ \"code\": \"C013\", \"message\": \"이미 등록된 학번입니다.\" }")
                }))
    })
    @PostMapping("/apply")
    public ResponseEntity<Void> apply(@Valid @RequestBody final ApplicantCreateRequestDto request) {
        applicantService.apply(request);
        return ResponseEntity.ok()
            .build();
    }

    // 모집 일정 조회
    @Operation(summary = "모집 일정 조회")
    @ApiResponse(responseCode = "200", description = "모집 일정 조회 성공")
    @GetMapping
    public ResponseEntity<List<RecruitConfigResponseDto>> getRecruitSchedule() {
        return ResponseEntity.ok(recruitService.getRecruitSchedule());
    }

    // 합격 결과 확인
    @Operation(summary = "합격 결과 확인")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "합격 결과 확인 성공"),
        @ApiResponse(responseCode = "400", description = "실패 케이스",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "학번 조회 실패 혹은 전화번호 뒷자리 일치하지 않음",
                        value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }")
                }))
    })
    @GetMapping("/result")
    public ResponseEntity<ResultCheckResponseDto> checkResult(@ModelAttribute final ResultCheckRequestDto request) {
        return ResponseEntity.ok(recruitService.checkResult(request));
    }

    // 면접 일정 생성
    @Operation(summary = "면접 일정 생성", description = "새로운 면접 일정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "면접 일정 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/interview/schedule")
    public ResponseEntity<List<InterviewSlotResponseDto>> createInterviewSlots(@Valid @RequestBody InterviewSlotCreateRequestDto request) {

        List<InterviewSlotResponseDto> slots =
                interviewService.createInterviewSlots(request.getStartDate(), request.getEndDate(), request.getStartTime(), request.getEndTime());
        return ResponseEntity.ok(slots);
    }

    // 면접 예약
    @Operation(summary = "면접 예약", description = "지원자가 특정 면접 슬롯을 예약합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "면접 예약 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/interview/reserve")
    public ResponseEntity<Void> reserveInterviewSlot(@Valid @RequestBody InterviewReservationRequestDto request) {
        interviewService.reserveInterviewSlot(request);
        return ResponseEntity.ok().build();
    }


}
