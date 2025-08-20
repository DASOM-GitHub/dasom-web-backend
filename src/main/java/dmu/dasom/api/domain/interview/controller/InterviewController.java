package dmu.dasom.api.domain.interview.controller;

import dmu.dasom.api.domain.interview.dto.InterviewReservationModifyRequestDto;
import dmu.dasom.api.domain.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Interview", description = "면접 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    @Operation(summary = "면접 예약 수정", description = "학번과 이메일 인증 후 면접 날짜 및 시간을 수정합니다.")
    @PutMapping("/reservation/modify")
    public ResponseEntity<Void> modifyInterviewReservation(@RequestBody InterviewReservationModifyRequestDto request) {
        interviewService.modifyInterviewReservation(request);
        return ResponseEntity.ok().build();
    }
}
