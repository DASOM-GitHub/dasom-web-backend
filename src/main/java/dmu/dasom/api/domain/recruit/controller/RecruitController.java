package dmu.dasom.api.domain.recruit.controller;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.service.ApplicantService;
import dmu.dasom.api.domain.common.exception.ErrorResponse;
import dmu.dasom.api.domain.interview.dto.*;
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
@Tag(name = "Recruit API", description = "부원 모집 API")
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
                                    @ExampleObject(name = "학번 중복", value = "{ \"code\": \"C013\", \"message\": \"이미 등록된 학번입니다.\" }"),
                                    @ExampleObject(name = "모집 기간 아님", value = "{ \"code\": \"C029\", \"message\": \"모집 기간이 아닙니다.\" }"),
                                    @ExampleObject(name = "잘못된 요청 값", value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }")
                            }))
    })
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

    // 모집 일정 수정
    @Operation(summary = "모집 일정 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모집 일정 수정 성공"),
            @ApiResponse(responseCode = "400", description = "실패 케이스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "잘못된 날짜 형식", value = "{ \"code\": \"C016\", \"message\": \"날짜 형식이 올바르지 않습니다.\" }"),
                                    @ExampleObject(name = "잘못된 시간 형식", value = "{ \"code\": \"C017\", \"message\": \"시간 형식이 올바르지 않습니다.\" }"),
                                    @ExampleObject(name = "잘못된 요청 값", value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }")
                            }))
    })
    @PutMapping("/schedule")
    public ResponseEntity<Void> modifyRecruitSchedule(@RequestBody dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto request) {
        recruitService.modifyRecruitSchedule(request);
        return ResponseEntity.ok().build();
    }

    // 모집 일정 초기화 (테스트용)
    @Operation(summary = "TEMP: 모집 일정 초기화")
    @GetMapping("/init-schedule")
    public ResponseEntity<String> initSchedule() {
        recruitService.initRecruitSchedule();
        return ResponseEntity.ok("Recruit schedule initialized successfully.");
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
                                    @ExampleObject(name = "지원자 없음", value = "{ \"code\": \"C022\", \"message\": \"지원자를 찾을 수 없습니다.\" }"),
                                    @ExampleObject(name = "잘못된 요청 값", value = "{ \"code\": \"C007\", \"message\": \"요청한 값이 올바르지 않습니다.\" }")
                            }))
    })
    @GetMapping("/result")
    public ResponseEntity<ResultCheckResponseDto> checkResult(@ModelAttribute final ResultCheckRequestDto request) {
        return ResponseEntity.ok(applicantService.checkResult(request));
    }

    // 면접 예약
    @Operation(summary = "면접 예약", description = "지원자가 특정 면접 슬롯을 예약합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "면접 예약 성공"),
            @ApiResponse(responseCode = "400", description = "실패 케이스",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "지원자 없음", value = "{ \"code\": \"C022\", \"message\": \"지원자를 찾을 수 없습니다.\" }"),
                                    @ExampleObject(name = "슬롯 없음", value = "{ \"code\": \"C021\", \"message\": \"슬롯을 찾을 수 없습니다.\" }"),
                                    @ExampleObject(name = "이미 예약됨", value = "{ \"code\": \"C023\", \"message\": \"이미 예약된 지원자입니다.\" }"),
                                    @ExampleObject(name = "슬롯 가득 참", value = "{ \"code\": \"C025\", \"message\": \"해당 슬롯이 가득 찼습니다.\" }"),
                                    @ExampleObject(name = "슬롯 불가", value = "{ \"code\": \"C33\", \"message\": \"해당 슬롯을 예약할 수 없습니다.\" }")
                            }))
    })
    @PostMapping("/interview/reserve")
    public ResponseEntity<Void> reserveInterviewSlot(@Valid @RequestBody InterviewReservationRequestDto request) {
        interviewService.reserveInterviewSlot(request);
        return ResponseEntity.ok().build();
    }

    // 예약 가능한 면접 일정 조회
    @Operation(summary = "예약 가능한 면접 일정 조회", description = "예약 가능한 면접 슬롯 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "예약 가능한 면접 슬롯 조회 성공")
    @GetMapping("/interview/available")
    public ResponseEntity<List<InterviewSlotResponseDto>> getAvailableInterviewSlots() {
        List<InterviewSlotResponseDto> availableSlots = interviewService.getAvailableSlots();
        return ResponseEntity.ok(availableSlots);
    }

    // 모든 면접 일정 조회
    @Operation(summary = "모든 면접 일정 조회", description = "모든 면접 슬롯 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "모든 면접 슬롯 조회 성공")
    @GetMapping("/interview/all")
    public ResponseEntity<List<InterviewSlotResponseDto>> getAllInterviewSlots() {
        List<InterviewSlotResponseDto> allSlots = interviewService.getAllInterviewSlots();
        return ResponseEntity.ok(allSlots);
    }
}
