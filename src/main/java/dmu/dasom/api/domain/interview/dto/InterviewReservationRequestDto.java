package dmu.dasom.api.domain.interview.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReservationRequestDto {

    private Long slotId; // 예약할 슬롯 ID

    private Long applicantId; // 지원자 ID

    private String reservationCode; // 학번 뒤 4자리 + 전화번호 뒤 4자리 조합 코드
}
