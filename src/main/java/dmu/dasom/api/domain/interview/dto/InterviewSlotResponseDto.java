package dmu.dasom.api.domain.interview.dto;

import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlotResponseDto {

    private Long id; // 슬롯 ID
    private LocalDate interviewDate; // 면접 날짜
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime; // 종료 시간
    private Integer maxCandidates; // 최대 지원자 수
    private Integer currentCandidates; // 현재 예약된 지원자 수

    public InterviewSlotResponseDto(InterviewSlot slot){
        this.id = slot.getId();
        this.interviewDate = slot.getInterviewDate();
        this.startTime = slot.getStartTime();
        this.endTime = slot.getEndTime();
        this.maxCandidates = slot.getMaxCandidates();
        this.currentCandidates = slot.getCurrentCandidates();
    }

}
