package dmu.dasom.api.domain.interview.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlotRequestDto {

    private LocalDate interviewDate; // 면접 날짜

    private LocalTime startTime; // 시작 시간

    private LocalTime endTime; // 종료 시간

    private Integer maxCandidates; // 최대 지원자 수
}
