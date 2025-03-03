package dmu.dasom.api.global.admin.controller;

import dmu.dasom.api.domain.interview.dto.InterviewReservationApplicantResponseDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotCreateRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotResponseDto;
import dmu.dasom.api.domain.interview.service.InterviewService;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/recruit")
@RequiredArgsConstructor
@Tag(name = "ADMIN - Recruit API", description = "어드민 모집 관리 API")
public class AdminRecruitController {

    private final RecruitService recruitService;
    private final InterviewService interviewService;

    @Operation(summary = "모집 일정 수정")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모집 일정 수정 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "날짜 형식 오류",
                        value = "{ \"code\": \"C016\", \"message\": \"날짜 형식이 올바르지 않습니다.\" }"
                    ),
                    @ExampleObject(
                        name = "시간 형식 오류",
                        value = "{ \"code\": \"C017\", \"message\": \"시간 형식이 올바르지 않습니다.\" }"
                    )
                }
            )
        )
    })
    @PatchMapping("/schedule")
    public ResponseEntity<Void> modifyRecruitSchedule(@Valid @RequestBody final RecruitScheduleModifyRequestDto request) {
        recruitService.modifyRecruitSchedule(request);
        return ResponseEntity.ok()
            .build();
    }

    @Operation(summary = "면접 일정 생성", description = "새로운 면접 일정을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "면접 일정 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/interview/schedule")
    public ResponseEntity<List<InterviewSlotResponseDto>> createInterviewSlots(@Valid @RequestBody InterviewSlotCreateRequestDto request) {

        List<InterviewSlotResponseDto> slots =
            interviewService.createInterviewSlots(
                request.getStartDate(),
                request.getEndDate(),
                request.getStartTime(),
                request.getEndTime()
            );
        return ResponseEntity.ok(slots);
    }

    @Operation(summary = "면접 예약자 조회")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "면접 예약자 조회 성공")
    })
    @GetMapping("/interview/applicants")
    public ResponseEntity<List<InterviewReservationApplicantResponseDto>> getAllInterviewApplicants() {
        List<InterviewReservationApplicantResponseDto> applicants = interviewService.getAllInterviewApplicants();
        return ResponseEntity.ok(applicants);
    }

}
