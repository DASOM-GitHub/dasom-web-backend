package dmu.dasom.api.domain.interview.service;

import dmu.dasom.api.domain.interview.dto.InterviewReservationRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public interface InterviewService {

    // 면접 슬롯 생성
    List<InterviewSlotResponseDto> createInterviewSlots(LocalDate newStartDate, LocalDate newEndDate, LocalTime newStartTime, LocalTime newEndTime);

    // 예약 가능한 면접 슬롯 조회
    List<InterviewSlotResponseDto> getAvailableSlots();

    // 면접 예약
    void reserveInterviewSlot(InterviewReservationRequestDto request);

    // 면접 예약 취소
    void cancelReservation(Long reservationId, Long applicantId);

    List<InterviewSlotResponseDto> getAllInterviewSlots();


}
