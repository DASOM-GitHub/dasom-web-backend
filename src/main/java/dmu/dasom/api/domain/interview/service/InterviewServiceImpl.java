package dmu.dasom.api.domain.interview.service;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import dmu.dasom.api.domain.applicant.repository.ApplicantRepository;
import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.interview.dto.InterviewReservationRequestDto;
import dmu.dasom.api.domain.interview.dto.InterviewSlotResponseDto;
import dmu.dasom.api.domain.interview.entity.InterviewReservation;
import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import dmu.dasom.api.domain.interview.enums.Status;
import dmu.dasom.api.domain.interview.repositoty.InterviewReservationRepository;
import dmu.dasom.api.domain.interview.repositoty.InterviewSlotRepository;
import dmu.dasom.api.domain.recruit.service.RecruitServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class InterviewServiceImpl implements InterviewService{

    private final InterviewSlotRepository interviewSlotRepository;
    private final InterviewReservationRepository interviewReservationRepository;
    private final ApplicantRepository applicantRepository;
    private final RecruitServiceImpl recruitService;

    // 면접 슬롯 생성
    @Override
    @Transactional
    public List<InterviewSlotResponseDto> createInterviewSlots(LocalDate newStartDate, LocalDate newEndDate, LocalTime newStartTime, LocalTime newEndTime) {
        boolean slotsExist = interviewSlotRepository.exists();

        if(slotsExist){
            interviewSlotRepository.deleteAll();
        }

        List<InterviewSlotResponseDto> newSlots = new ArrayList<>();
        for(LocalDate date = newStartDate; !date.isAfter(newEndDate); date = date.plusDays(1)){
            LocalTime currentTime = newStartTime;
            while (currentTime.isBefore(newEndTime)){
                LocalTime slotEndTime = currentTime.plusMinutes(20);

                InterviewSlot slot = InterviewSlot.builder()
                        .interviewDate(date)
                        .startTime(currentTime)
                        .endTime(slotEndTime)
                        .maxCandidates(2)
                        .currentCandidates(0)
                        .status(Status.ACTIVE)
                        .build();

                interviewSlotRepository.save(slot);
                newSlots.add(new InterviewSlotResponseDto(slot));
                currentTime = slotEndTime;
            }
        }

        return newSlots;
    }

    // 예약 가능한 면접 슬롯 조회
    @Override
    public List<InterviewSlotResponseDto> getAvailableSlots() {
        return interviewSlotRepository.findAllByStatusAndCurrentCandidatesLessThanMaxCandidates(Status.ACTIVE)
                .stream()
                .map(InterviewSlotResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void reserveInterviewSlot(InterviewReservationRequestDto request) {
        InterviewSlot slot = interviewSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new CustomException(ErrorCode.SLOT_NOT_FOUND));

        if(!slot.getStatus().equals(Status.ACTIVE)){
            throw new CustomException(ErrorCode.SLOT_NOT_ACTIVE);
        }

        if (slot.getCurrentCandidates() >= slot.getMaxCandidates()) {
            throw new CustomException(ErrorCode.SLOT_FULL);
        }

        Applicant applicant = applicantRepository.findById(request.getApplicantId())
                .orElseThrow(() -> new CustomException(ErrorCode.APPLICANT_NOT_FOUND));

        boolean alreadyReserved = interviewReservationRepository.existsByReservationCode(request.getReservationCode());
        if (alreadyReserved) {
            throw new CustomException(ErrorCode.ALREADY_RESERVED);
        }

        // 예약 정보 저장
        InterviewReservation reservation = InterviewReservation.builder()
                .slot(slot)
                .applicant(applicant)
                .reservationCode(request.getReservationCode())
                .build();

        interviewReservationRepository.save(reservation);

        // 현재 예약 인원 증가
        slot.incrementCurrentCandidates();
    }

    // 면접 예약 취소
    @Override
    @Transactional
    public void cancelReservation(Long reservationId, Long applicantId) {
        InterviewReservation reservation = interviewReservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!reservation.getApplicant().getId().equals(applicantId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 현재 예약 인원 감소
        InterviewSlot slot = reservation.getSlot();
        slot.decrementCurrentCandidates();

        // 예약 삭제
        interviewReservationRepository.delete(reservation);
    }

}
