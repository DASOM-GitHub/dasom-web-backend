package dmu.dasom.api.domain.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InterviewSlotCreateRequestDto", description = "면접 슬롯 생성 요청 DTO")
public class InterviewReservationResponseDto {

    @NotNull(message = "예약 ID는 필수 값입니다.")
    @Schema(description = "예약의 고유 ID", example = "1")
    private Long reservationId; // 예약 ID

    @NotNull(message = "슬롯 ID는 필수 값입니다.")
    @Schema(description = "예약된 면접 슬롯의 ID", example = "10")
    private Long slotId; // 슬롯 ID

    @NotNull(message = "지원자 ID는 필수 값입니다.")
    @Schema(description = "예약한 지원자의 ID", example = "1001")
    private Long applicantId; // 지원자 ID

    @NotNull(message = "예약 코드는 필수 값입니다.")
    @Schema(description = "학번 전체와 전화번호 뒤 4자리로 구성된 예약 코드", example = "202500010542")
    private String reservationCode; // 예약 코드 (학번+전화번호 조합)

}
