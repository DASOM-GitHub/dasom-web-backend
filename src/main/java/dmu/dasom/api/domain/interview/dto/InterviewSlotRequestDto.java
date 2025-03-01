package dmu.dasom.api.domain.interview.dto;

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
public class InterviewSlotRequestDto {

    @NotNull(message = "면접 날짜는 필수 입력 값입니다.")
    private LocalDate interviewDate; // 면접 날짜

    @NotNull(message = "시작 시간은 필수 입력 값입니다.")
    private LocalTime startTime; // 시작 시간

    @NotNull(message = "종료 시간은 필수 입력 값입니다.")
    private LocalTime endTime; // 종료 시간

    @NotNull(message = "최대 지원자 수는 필수 입력 값입니다.")
    @Min(value = 1, message = "최대 지원자 수는 최소 1명 이상이어야 합니다.")
    @Max(value = 100, message = "최대 지원자 수는 최대 100명까지 가능합니다.") // 필요에 따라 수정 가능
    private Integer maxCandidates; // 최대 지원자 수
}
