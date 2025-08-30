package dmu.dasom.api.domain.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InterviewReservationModifyRequestDto", description = "면접 예약 수정 요청 DTO")
public class InterviewReservationModifyRequestDto {

    @NotNull(message = "학번은 필수 값입니다.")
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자로 구성되어야 합니다.")
    @Size(min = 8, max = 8)
    @Schema(description = "지원자 학번", example = "20250001")
    private String studentNo;

    @NotNull(message = "이메일은 필수 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 64)
    @Schema(description = "지원자 이메일", example = "test@example.com")
    private String email;

    @NotNull(message = "새로운 슬롯 ID는 필수 값입니다.")
    @Schema(description = "새롭게 예약할 면접 슬롯의 ID", example = "2")
    private Long newSlotId;

    @NotNull(message = "인증 코드는 필수 값입니다.")
    @Schema(description = "이메일로 발송된 6자리 인증 코드", example = "123456")
    private String verificationCode;
}
