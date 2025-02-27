package dmu.dasom.api.domain.recruit.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class RecruitScheduleDto {
    private LocalDate recruitStartDate;     // 모집 시작 날짜
    private LocalDate recruitEndDate;       // 모집 종료 날짜
    private LocalDate interviewStartDate;   // 면접 시작 날짜
    private LocalDate interviewEndDate;     // 면접 종료 날짜
    private LocalTime interviewStartTime;   // 면접 시작 시간
    private LocalTime interviewEndTime;     // 면접 종료 시간
    private LocalDate documentPassDate;     // 1차 합격 발표 날짜
    private LocalDate finalPassDate;        // 최종 합격 발표 날짜
}
