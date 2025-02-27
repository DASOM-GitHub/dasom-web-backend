package dmu.dasom.api.domain.recruit;

import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import dmu.dasom.api.domain.applicant.service.ApplicantServiceImpl;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.interview.dto.InterviewReservationRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotResponseDto;
import dmu.dasom.api.domain.interview.service.InterviewServiceImpl;
import dmu.dasom.api.domain.recruit.dto.ResultCheckRequestDto;
import dmu.dasom.api.domain.recruit.dto.ResultCheckResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitConfigResponseDto;
import dmu.dasom.api.domain.recruit.dto.RecruitScheduleModifyRequestDto;
import dmu.dasom.api.domain.recruit.entity.Recruit;
import dmu.dasom.api.domain.recruit.enums.ConfigKey;
import dmu.dasom.api.domain.recruit.enums.ResultCheckType;
import dmu.dasom.api.domain.recruit.repository.RecruitRepository;
import dmu.dasom.api.domain.recruit.service.RecruitServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RecruitServiceTest {

    @Mock
    private RecruitRepository recruitRepository;

    @Mock
    private ApplicantServiceImpl applicantService;

    @Mock
    private InterviewServiceImpl interviewService;

    @InjectMocks
    private RecruitServiceImpl recruitService;

    @Test
    @DisplayName("모집 일정 조회")
    void getRecruitSchedule() {
        // given
        Recruit recruit1 = mock(Recruit.class);
        Recruit recruit2 = mock(Recruit.class);
        when(recruitRepository.findAll()).thenReturn(List.of(recruit1, recruit2));

        // when
        List<RecruitConfigResponseDto> schedule = recruitService.getRecruitSchedule();

        // then
        assertNotNull(schedule);
        assertEquals(2, schedule.size());
        verify(recruitRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("모집 일정 수정 - 성공")
    void modifyRecruitSchedule_success() {
        // given
        Recruit recruit = mock(Recruit.class);
        RecruitScheduleModifyRequestDto request = mock(RecruitScheduleModifyRequestDto.class);
        when(request.getKey()).thenReturn(ConfigKey.RECRUITMENT_PERIOD_START);
        when(request.getValue()).thenReturn("2025-01-02T12:00:00");
        when(recruitRepository.findByKey(ConfigKey.RECRUITMENT_PERIOD_START)).thenReturn(Optional.of(recruit));

        // when
        recruitService.modifyRecruitSchedule(request);

        // then
        verify(recruit, times(1)).updateDateTime(LocalDateTime.of(2025, 1, 2, 12, 0, 0));
    }

    @Test
    @DisplayName("모집 일정 수정 - 실패")
    void modifyRecruitSchedule_fail() {
        // given
        RecruitScheduleModifyRequestDto request = mock(RecruitScheduleModifyRequestDto.class);
        when(request.getKey()).thenReturn(ConfigKey.INTERVIEW_TIME_START);
        when(request.getValue()).thenReturn("12:00");
        when(recruitRepository.findByKey(ConfigKey.INTERVIEW_TIME_START)).thenReturn(Optional.of(mock(Recruit.class)));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            recruitService.modifyRecruitSchedule(request);
        });

        // then
        assertEquals(ErrorCode.INVALID_TIME_FORMAT, exception.getErrorCode());
    }

    @Test
    @DisplayName("합격 결과 확인 - 성공")
    void checkResult_success() {
        // given
        Recruit recruit = mock(Recruit.class);
        when(recruitRepository.findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT)).thenReturn(Optional.of(recruit));
        String pastDateTime = LocalDateTime.now().minusHours(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        when(recruit.getValue()).thenReturn(pastDateTime);

        ResultCheckRequestDto request = mock(ResultCheckRequestDto.class);
        when(request.getType()).thenReturn(ResultCheckType.DOCUMENT_PASS);
        when(request.getStudentNo()).thenReturn("20210000");
        when(request.getContactLastDigit()).thenReturn("1234");

        ApplicantDetailsResponseDto applicant = mock(ApplicantDetailsResponseDto.class);
        when(applicant.getContact()).thenReturn("010-5678-1234");
        when(applicant.getStatus()).thenReturn(ApplicantStatus.DOCUMENT_PASSED);
        when(applicant.getStudentNo()).thenReturn("20210000");
        when(applicant.getName()).thenReturn("TestName");
        when(applicantService.getApplicantByStudentNo("20210000")).thenReturn(applicant);

        // when
        ResultCheckResponseDto result = recruitService.checkResult(request);

        // then
        assertNotNull(result);
        assertTrue(result.getIsPassed());
        verify(recruitRepository, times(1)).findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT);
        verify(applicantService, times(1)).getApplicantByStudentNo("20210000");
    }

    @Test
    @DisplayName("합격 결과 확인 - 실패 (확인 기간 아님)")
    void checkResult_fail_inquiryPeriod() {
        // given
        Recruit recruit = mock(Recruit.class);
        when(recruitRepository.findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT)).thenReturn(Optional.of(recruit));
        String futureDateTime = LocalDateTime.now().plusHours(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        when(recruit.getValue()).thenReturn(futureDateTime);

        ResultCheckRequestDto request = mock(ResultCheckRequestDto.class);
        when(request.getType()).thenReturn(ResultCheckType.DOCUMENT_PASS);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            recruitService.checkResult(request);
        });

        // then
        assertEquals(ErrorCode.INVALID_INQUIRY_PERIOD, exception.getErrorCode());
        verify(recruitRepository, times(1)).findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT);
    }

    @Test
    @DisplayName("합격 결과 확인 - 실패 (연락처 뒷자리 불일치)")
    void checkResult_fail_contactMismatch() {
        // given
        Recruit recruit = mock(Recruit.class);
        when(recruitRepository.findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT)).thenReturn(Optional.of(recruit));
        String pastDateTime = LocalDateTime.now().minusHours(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        when(recruit.getValue()).thenReturn(pastDateTime);

        ResultCheckRequestDto request = mock(ResultCheckRequestDto.class);
        when(request.getType()).thenReturn(ResultCheckType.DOCUMENT_PASS);
        when(request.getStudentNo()).thenReturn("20210000");
        when(request.getContactLastDigit()).thenReturn("0000");

        ApplicantDetailsResponseDto applicant = mock(ApplicantDetailsResponseDto.class);
        when(applicant.getContact()).thenReturn("010-5678-1234");
        when(applicantService.getApplicantByStudentNo("20210000")).thenReturn(applicant);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            recruitService.checkResult(request);
        });

        // then
        assertEquals(ErrorCode.ARGUMENT_NOT_VALID, exception.getErrorCode());
        verify(recruitRepository, times(1)).findByKey(ConfigKey.DOCUMENT_PASS_ANNOUNCEMENT);
        verify(applicantService, times(1)).getApplicantByStudentNo("20210000");
    }

    @Test
    @DisplayName("면접 일정 생성 - 성공")
    void createInterviewSlots_success() {
        // given
        LocalDate startDate = LocalDate.of(2025, 3, 12);
        LocalDate endDate = LocalDate.of(2025, 3, 14);
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(20, 0);

        InterviewSlotResponseDto slot1 = mock(InterviewSlotResponseDto.class);
        InterviewSlotResponseDto slot2 = mock(InterviewSlotResponseDto.class);

        when(interviewService.createInterviewSlots(startDate, endDate, startTime, endTime))
                .thenReturn(List.of(slot1, slot2));

        // when
        List<InterviewSlotResponseDto> slots = interviewService.createInterviewSlots(startDate, endDate, startTime, endTime);

        // then
        assertNotNull(slots);
        assertEquals(2, slots.size());
        verify(interviewService, times(1)).createInterviewSlots(startDate, endDate, startTime, endTime);
    }

    @Test
    @DisplayName("면접 예약 - 성공")
    void reserveInterviewSlot_success() {
        // given
        InterviewReservationRequestDto request = new InterviewReservationRequestDto(1L, 1234L, "00006789");

        // when
        interviewService.reserveInterviewSlot(request);

        // then
        verify(interviewService, times(1)).reserveInterviewSlot(request);
    }


    @Test
    @DisplayName("면접 예약 - 실패 (슬롯 없음)")
    void reserveInterviewSlot_fail_slotNotFound() {
        // given
        InterviewReservationRequestDto request = new InterviewReservationRequestDto(1L, 1234L, "00006789");

        doThrow(new CustomException(ErrorCode.SLOT_NOT_FOUND))
                .when(interviewService).reserveInterviewSlot(request);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            interviewService.reserveInterviewSlot(request);
        });

        // then
        assertEquals(ErrorCode.SLOT_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("면접 예약 - 실패 (최대 지원자 수 초과)")
    void reserveInterviewSlot_fail_slotFull() {
        // given
        InterviewReservationRequestDto request = new InterviewReservationRequestDto(1L, 1234L, "00006789");

        doThrow(new CustomException(ErrorCode.SLOT_FULL))
                .when(interviewService).reserveInterviewSlot(request);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            interviewService.reserveInterviewSlot(request);
        });

        // then
        assertEquals(ErrorCode.SLOT_FULL, exception.getErrorCode());
    }


    @Test
    @DisplayName("면접 예약 - 실패 (이미 예약됨)")
    void reserveInterviewSlot_fail_alreadyReserved() {
        // given
        InterviewReservationRequestDto request = new InterviewReservationRequestDto(1L, 1234L, "00006789");

        doThrow(new CustomException(ErrorCode.ALREADY_RESERVED))
                .when(interviewService).reserveInterviewSlot(request);

        // when
        CustomException exception = assertThrows(CustomException.class, () -> {
            interviewService.reserveInterviewSlot(request);
        });

        // then
        assertEquals(ErrorCode.ALREADY_RESERVED, exception.getErrorCode());
    }


}
