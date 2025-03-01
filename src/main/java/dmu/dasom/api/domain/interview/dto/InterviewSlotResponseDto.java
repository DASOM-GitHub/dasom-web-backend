package dmu.dasom.api.domain.interview.dto;

import dmu.dasom.api.domain.interview.entity.InterviewSlot;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "InterviewSlotResponseDto", description = "면접 슬롯 응답 DTO")
public class InterviewSlotResponseDto {

    @NotNull(message = "슬롯 ID는 필수 입력 값입니다.")
    @Schema(description = "슬롯의 고유 ID", example = "1")
    private Long id; // 슬롯 ID

    @NotNull(message = "면접 날짜는 필수 입력 값입니다.")
    @Schema(description = "면접이 진행되는 날짜", example = "2025-03-12")
    private LocalDate interviewDate; // 면접 날짜

    @NotNull(message = "시작 시간은 필수 입력 값입니다.")
    @Schema(description = "면접 시작 시간", example = "10:00")
    private LocalTime startTime; // 시작 시간

    @NotNull(message = "종료 시간은 필수 입력 값입니다.")
    @Schema(description = "면접 종료 시간", example = "10:20")
    private LocalTime endTime; // 종료 시간

    @NotNull(message = "최대 지원자 수는 필수 입력 값입니다.")
    @Min(value = 1, message = "최대 지원자 수는 최소 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 지원자 수는 최대 100명까지 가능합니다.")
    @Schema(description = "해당 슬롯에서 허용되는 최대 지원자 수", example = "2")
    private Integer maxCandidates; // 최대 지원자 수

    @NotNull(message = "현재 예약된 지원자 수는 필수 입력 값입니다.")
    @Min(value = 0, message = "현재 예약된 지원자 수는 0명 이상이어야 합니다.")
    @Schema(description = "현재 해당 슬롯에 예약된 지원자 수", example = "1")
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
