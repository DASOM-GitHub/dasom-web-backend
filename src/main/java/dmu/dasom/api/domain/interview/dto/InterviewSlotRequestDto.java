package dmu.dasom.api.domain.interview.dto;

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
@Schema(name = "InterviewSlotRequestDto", description = "면접 슬롯 요청 DTO")
public class InterviewSlotRequestDto {

    @NotNull(message = "면접 날짜는 필수 입력 값입니다.")
    @Schema(description = "면접이 진행되는 날짜", example = "2025-03-12")
    private LocalDate interviewDate; // 면접 날짜

    @NotNull(message = "시작 시간은 필수 입력 값입니다.")
    @Schema(description = "면접 시작 시간", example = "10:00")
    private LocalTime startTime; // 시작 시간

    @NotNull(message = "종료 시간은 필수 입력 값입니다.")
    @Schema(description = "면접 종료 시간", example = "17:00")
    private LocalTime endTime; // 종료 시간

    @NotNull(message = "최대 지원자 수는 필수 입력 값입니다.")
    @Min(value = 1, message = "최대 지원자 수는 최소 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 지원자 수는 최대 100명까지 가능합니다.")
    @Schema(description = "해당 슬롯에서 허용되는 최대 지원자 수", example = "2")
    private Integer maxCandidates; // 최대 지원자 수
}
