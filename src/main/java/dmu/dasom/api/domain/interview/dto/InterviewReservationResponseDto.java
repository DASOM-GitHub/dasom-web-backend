package dmu.dasom.api.domain.interview.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReservationResponseDto {

    private Long reservationId; // 예약 ID

    private Long slotId; // 슬롯 ID

    private Long applicantId; // 지원자 ID

    private String reservationCode; // 예약 코드 (학번+전화번호 조합)

}
