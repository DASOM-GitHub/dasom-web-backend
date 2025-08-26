package dmu.dasom.api.domain.interview.controller;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.google.service.EmailService;

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
    private final EmailService emailService; // 이메일 발송 서비스 (Google 기반)

    /*
     * 면접 예약 수정을 위한 인증 코드 발송
     * - 지원자의 학번을 입력받아 해당 지원자를 조회
     * - VerificationCodeManager를 통해 인증 코드 생성 및 Redis 저장
     * - EmailService를 이용해 지원자 이메일로 인증 코드 발송
     */
    @Operation(summary = "면접 예약 수정을 위한 인증 코드 발송", description = "지원자의 학번을 받아 이메일로 인증 코드를 발송합니다.")
    @PostMapping("/send-verification")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody VerificationCodeRequestDto request) throws MessagingException {
        // 학번으로 지원자 조회 (없으면 예외 발생)
        Applicant applicant = applicantRepository.findByStudentNo(request.getStudentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICANT_NOT_FOUND));

        // 인증 코드 생성 후 Redis에 저장
        String code = verificationCodeManager.generateAndStoreCode(applicant.getStudentNo());

        // 이메일 발송 (받는 사람 이메일, 이름, 코드 전달)
        emailService.sendVerificationEmail(applicant.getEmail(), applicant.getName(), code);

        return ResponseEntity.ok().build();
    }

    /*
     * 면접 예약 수정
     * - 사용자가 받은 인증 코드를 검증한 후
     * - InterviewService를 통해 예약 날짜/시간 수정 처리
     */
    @Operation(summary = "면접 예약 수정", description = "이메일로 발송된 인증 코드를 통해 인증 후, 면접 날짜 및 시간을 수정합니다.")
    @PutMapping("/reservation/modify")
    public ResponseEntity<Void> modifyInterviewReservation(@Valid @RequestBody InterviewReservationModifyRequestDto request) {
        interviewService.modifyInterviewReservation(request);
        return ResponseEntity.ok().build();
    }

    /*
     * 모든 면접 지원자 조회
     * - InterviewService를 통해 모든 지원자 + 예약 정보 반환
     */
    @Operation(summary = "모든 면접 지원자 목록 조회", description = "모든 면접 지원자의 상세 정보와 예약 정보를 조회합니다.")
    @GetMapping("/applicants")
    public ResponseEntity<List<InterviewReservationApplicantResponseDto>> getAllInterviewApplicants() {
        List<InterviewReservationApplicantResponseDto> applicants = interviewService.getAllInterviewApplicants();
        return ResponseEntity.ok(applicants);
    }
}
