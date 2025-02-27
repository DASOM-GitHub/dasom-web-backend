package dmu.dasom.api.domain.interview.entity;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private InterviewSlot slot; // 연관된 면접 슬롯

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant; // 지원자

    @Column(nullable = false, unique = true, length = 8)
    private String reservationCode; // 학번 뒤 4자리 + 전화번호 뒤 4자리 조합 코드
}
