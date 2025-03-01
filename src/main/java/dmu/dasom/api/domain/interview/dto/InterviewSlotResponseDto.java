package dmu.dasom.api.domain.interview.dto;

import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlotResponseDto {

    @NotNull(message = "슬롯 ID는 필수 입력 값입니다.")
    private Long id; // 슬롯 ID

    @NotNull(message = "면접 날짜는 필수 입력 값입니다.")
    private LocalDate interviewDate; // 면접 날짜

    @NotNull(message = "시작 시간은 필수 입력 값입니다.")
    private LocalTime startTime; // 시작 시간

    @NotNull(message = "종료 시간은 필수 입력 값입니다.")
    private LocalTime endTime; // 종료 시간

    @NotNull(message = "최대 지원자 수는 필수 입력 값입니다.")
    @Min(value = 1, message = "최대 지원자 수는 최소 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 지원자 수는 최대 100명까지 가능합니다.")
    private Integer maxCandidates; // 최대 지원자 수

    @NotNull(message = "현재 예약된 지원자 수는 필수 입력 값입니다.")
    @Min(value = 0, message = "현재 예약된 지원자 수는 0명 이상이어야 합니다.")
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
