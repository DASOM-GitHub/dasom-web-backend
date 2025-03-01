package dmu.dasom.api.domain.interview.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewReservationRequestDto {

    @NotNull(message = "슬롯 ID는 필수 값입니다.")
    private Long slotId; // 예약할 슬롯 ID

    @NotNull(message = "예약 코드는 필수 값입니다.")
    @Pattern(regexp = "^[0-9]{8}[0-9]{4}$", message = "예약 코드는 학번 전체와 전화번호 뒤 4자리로 구성되어야 합니다.")
    private String reservationCode; // 학번 전체 + 전화번호 뒤 4자리 조합 코드
}
