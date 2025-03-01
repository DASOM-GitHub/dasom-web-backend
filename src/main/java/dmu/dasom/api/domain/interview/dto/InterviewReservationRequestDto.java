package dmu.dasom.api.domain.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InterviewReservationResponseDto", description = "면접 예약 응답 DTO")
public class InterviewReservationRequestDto {

    @NotNull(message = "슬롯 ID는 필수 값입니다.")
    @Schema(description = "예약할 면접 슬롯의 ID", example = "1")
    private Long slotId; // 예약할 슬롯 ID

    @NotNull(message = "예약 코드는 필수 값입니다.")
    @Pattern(regexp = "^[0-9]{8}[0-9]{4}$", message = "예약 코드는 학번 전체와 전화번호 뒤 4자리로 구성되어야 합니다.")
    @Schema(description = "학번 전체와 전화번호 뒤 4자리로 구성된 예약 코드", example = "202500010542")
    private String reservationCode; // 학번 전체 + 전화번호 뒤 4자리 조합 코드
}
