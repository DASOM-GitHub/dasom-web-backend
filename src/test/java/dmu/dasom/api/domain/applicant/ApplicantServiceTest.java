package dmu.dasom.api.domain.applicant;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.applicant.service.ApplicantServiceImpl;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.email.enums.MailType;
import dmu.dasom.api.domain.email.service.EmailService;
import dmu.dasom.api.global.dto.PageResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicantServiceTest {

    @Mock
    private ApplicantRepository applicantRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ApplicantServiceImpl applicantService;

    @Test
    @DisplayName("지원자 저장 - 성공")
    void apply_success() {
        // given
        ApplicantCreateRequestDto request = mock(ApplicantCreateRequestDto.class);
        when(request.getStudentNo()).thenReturn("20210000");
        when(applicantRepository.findByStudentNo("20210000")).thenReturn(Optional.empty());

        // when
        applicantService.apply(request);

        // then
        verify(applicantRepository).save(request.toEntity());
    }

    @Test
    @DisplayName("지원자 저장 - 실패")
    void apply_fail() {
        // given
        ApplicantCreateRequestDto request = mock(ApplicantCreateRequestDto.class);
        when(request.getStudentNo()).thenReturn("20210000");
        when(applicantRepository.findByStudentNo("20210000")).thenReturn(Optional.of(mock(Applicant.class)));
        when(request.getIsOverwriteConfirmed()).thenReturn(false);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicantService.apply(request);
        });

        // then
        verify(applicantRepository).findByStudentNo("20210000");
        assertEquals(ErrorCode.DUPLICATED_STUDENT_NO, exception.getErrorCode());
    }

    @Test
    @DisplayName("지원자 저장 - 덮어쓰기")
    void apply_overwrite() {
        // given
        ApplicantCreateRequestDto request = mock(ApplicantCreateRequestDto.class);
        when(request.getStudentNo()).thenReturn("20210000");
        Applicant applicant = mock(Applicant.class);
        when(applicantRepository.findByStudentNo("20210000")).thenReturn(Optional.of(applicant));
        when(request.getIsOverwriteConfirmed()).thenReturn(true);

        // when
        applicantService.apply(request);

        // then
        verify(applicantRepository).findByStudentNo("20210000");
        verify(applicant).overwrite(request);
    }

    @Test
    @DisplayName("지원자 조회 - 성공")
    void getApplicants_success() {
        // given
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Applicant applicant = mock(Applicant.class);
        Page<Applicant> applicants = new PageImpl<>(Collections.singletonList(applicant), pageRequest, 1);
        when(applicantRepository.findAllWithPageRequest(pageRequest)).thenReturn(applicants);
        when(applicant.toApplicantResponse()).thenReturn(mock(ApplicantResponseDto.class));

        // when
        PageResponse<ApplicantResponseDto> response = applicantService.getApplicants(page);

        // then
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        verify(applicantRepository).findAllWithPageRequest(pageRequest);
    }

    @Test
    @DisplayName("지원자 조회 - 실패 (결과 없음)")
    void getApplicants_fail_emptyResult() {
        // given
        int page = 0;
        PageRequest pageRequest = PageRequest.of(page, 20);
        Page<Applicant> applicants = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(applicantRepository.findAllWithPageRequest(pageRequest)).thenReturn(applicants);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicantService.getApplicants(page);
        });

        // then
        assertEquals(ErrorCode.EMPTY_RESULT, exception.getErrorCode());
        verify(applicantRepository).findAllWithPageRequest(pageRequest);
    }

    @Test
    @DisplayName("메일 전송 - 서류 결과 메일 (DOCUMENT_RESULT)")
    void sendEmailsToApplicants_documentResult() throws MessagingException {
        // given
        MailType mailType = MailType.DOCUMENT_RESULT;
        Applicant applicant = mock(Applicant.class);
        when(applicantRepository.findAll()).thenReturn(Collections.singletonList(applicant));
        when(applicant.getEmail()).thenReturn("test@example.com");
        when(applicant.getName()).thenReturn("지원자");

        // when
        assertDoesNotThrow(() -> applicantService.sendEmailsToApplicants(mailType));

        // then
        verify(applicantRepository).findAll();
        verify(emailService).sendEmail("test@example.com", "지원자", mailType);
    }

    @Test
    @DisplayName("메일 전송 - 최종 결과 메일 (FINAL_RESULT)")
    void sendEmailsToApplicants_finalResult() throws MessagingException {
        // given
        MailType mailType = MailType.FINAL_RESULT;
        Applicant passedApplicant = mock(Applicant.class);
        Applicant failedApplicant = mock(Applicant.class);

        when(applicantRepository.findByStatusIn(
                List.of(ApplicantStatus.INTERVIEW_FAILED, ApplicantStatus.INTERVIEW_PASSED)))
                .thenReturn(List.of(passedApplicant, failedApplicant));

        when(passedApplicant.getEmail()).thenReturn("passed@example.com");
        when(passedApplicant.getName()).thenReturn("합격자");
        when(failedApplicant.getEmail()).thenReturn("failed@example.com");
        when(failedApplicant.getName()).thenReturn("불합격자");

        // when
        assertDoesNotThrow(() -> applicantService.sendEmailsToApplicants(mailType));

        // then
        verify(applicantRepository).findByStatusIn(
                List.of(ApplicantStatus.INTERVIEW_FAILED, ApplicantStatus.INTERVIEW_PASSED));
        verify(emailService).sendEmail("passed@example.com", "합격자", mailType);
        verify(emailService).sendEmail("failed@example.com", "불합격자", mailType);
    }

    @Test
    @DisplayName("메일 전송 - 잘못된 MailType")
    void sendEmailsToApplicants_invalidMailType() {
        // given
        MailType invalidMailType = null;

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicantService.sendEmailsToApplicants(invalidMailType);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

}