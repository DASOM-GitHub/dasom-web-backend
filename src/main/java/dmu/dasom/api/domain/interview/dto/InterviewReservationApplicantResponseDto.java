package dmu.dasom.api.domain.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "InterviewReservationApplicantResponseDto", description = "면접 예약 지원자 응답 DTO")
public class InterviewReservationApplicantResponseDto {

    @NotNull(message = "지원자 ID는 필수 값입니다.")
    @Schema(description = "지원자의 고유 ID", example = "1234")
    private Long applicantId;

    @NotNull(message = "이름은 필수 값입니다.")
    @Schema(description = "지원자 이름", example = "홍길동")
    private String applicantName;

    @NotNull(message = "학번은 필수 값입니다.")
    @Schema(description = "지원자 학번", example = "20210001")
    private String studentNo;

    @NotNull(message = "연락처는 필수 값입니다.")
    @Schema(description = "지원자 연락처", example = "010-1234-5678")
    private String contact;

    @NotNull(message = "이메일은 필수 값입니다.")
    @Schema(description = "지원자 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "희망 활동", example = "프로젝트")
    private String activityWish;

    @Schema(description = "지원 동기", example = "동아리 활동을 통해 새로운 경험을 쌓고 싶어서 지원합니다.")
    private String reasonForApply;
}
