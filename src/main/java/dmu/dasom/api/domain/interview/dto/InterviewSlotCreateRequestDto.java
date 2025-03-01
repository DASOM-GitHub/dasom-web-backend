package dmu.dasom.api.domain.interview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlotCreateRequestDto {

    @NotNull(message = "시작 날짜는 필수 값입니다.")
    private LocalDate startDate; // 면접 시작 날짜

    @NotNull(message = "종료 날짜는 필수 값입니다.")
    private LocalDate endDate; // 면접 종료 날짜

    @NotNull(message = "시작 시간은 필수 값입니다.")
    private LocalTime startTime; // 하루의 시작 시간

    @NotNull(message = "종료 시간은 필수 값입니다.")
    private LocalTime endTime; // 하루의 종료 시간
}