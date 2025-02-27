package dmu.dasom.api.domain.interview.entity;

import dmu.dasom.api.domain.interview.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate interviewDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime; // 종료 시간

    @Column(nullable = false)
    private Integer maxCandidates; // 최대 지원자 수

    @Column(nullable = false)
    private Integer currentCandidates; // 현재 예약된 지원자 수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Status status; // 면접 슬롯 상태 (ACTIVE, INACTIVE, CLOSED)

    public void incrementCurrentCandidates() {
        this.currentCandidates++;
        if (this.currentCandidates >= this.maxCandidates) {
            this.status = Status.CLOSED; // 최대 지원자 수에 도달하면 상태 변경
        }
    }

    public void decrementCurrentCandidates() {
        this.currentCandidates--;
        if (status == Status.CLOSED && this.currentCandidates < this.maxCandidates) {
            this.status = Status.ACTIVE; // 지원자 수가 줄어들면 다시 활성화
        }
    }

}
