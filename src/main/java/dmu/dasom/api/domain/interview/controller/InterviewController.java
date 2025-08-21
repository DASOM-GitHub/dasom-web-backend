package dmu.dasom.api.domain.interview.controller;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.email.service.EmailService;
import dmu.dasom.api.domain.interview.dto.InterviewReservationModifyRequestDto;
import dmu.dasom.api.domain.interview.dto.VerificationCodeRequestDto;
import dmu.dasom.api.domain.interview.service.InterviewService;
import dmu.dasom.api.global.util.VerificationCodeManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import dmu.dasom.api.domain.interview.dto.InterviewReservationApplicantResponseDto;

@Tag(name = "Interview", description = "면접 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;
    private final ApplicantRepository applicantRepository;
    private final VerificationCodeManager verificationCodeManager;
    private final EmailService emailService;

    @Operation(summary = "면접 예약 수정을 위한 인증 코드 발송", description = "지원자의 학번을 받아 이메일로 인증 코드를 발송합니다.")
    @PostMapping("/send-verification")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody VerificationCodeRequestDto request) throws MessagingException {
        Applicant applicant = applicantRepository.findByStudentNo(request.getStudentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICANT_NOT_FOUND));

        String code = verificationCodeManager.generateAndStoreCode(applicant.getStudentNo());
        emailService.sendVerificationEmail(applicant.getEmail(), applicant.getName(), code);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "면접 예약 수정", description = "이메일로 발송된 인증 코드를 통해 인증 후, 면접 날짜 및 시간을 수정합니다.")
    @PutMapping("/reservation/modify")
    public ResponseEntity<Void> modifyInterviewReservation(@Valid @RequestBody InterviewReservationModifyRequestDto request) {
        interviewService.modifyInterviewReservation(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 면접 지원자 목록 조회", description = "모든 면접 지원자의 상세 정보와 예약 정보를 조회합니다.")
    @GetMapping("/applicants")
    public ResponseEntity<List<InterviewReservationApplicantResponseDto>> getAllInterviewApplicants() {
        List<InterviewReservationApplicantResponseDto> applicants = interviewService.getAllInterviewApplicants();
        return ResponseEntity.ok(applicants);
    }
}
