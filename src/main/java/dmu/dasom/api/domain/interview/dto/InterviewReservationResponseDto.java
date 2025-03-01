package dmu.dasom.api.domain.interview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReservationResponseDto {

    @NotNull(message = "예약 ID는 필수 값입니다.")
    private Long reservationId; // 예약 ID

    @NotNull(message = "슬롯 ID는 필수 값입니다.")
    private Long slotId; // 슬롯 ID

    @NotNull(message = "지원자 ID는 필수 값입니다.")
    private Long applicantId; // 지원자 ID

    @NotNull(message = "예약 코드는 필수 값입니다.")
    private String reservationCode; // 예약 코드 (학번+전화번호 조합)

}
