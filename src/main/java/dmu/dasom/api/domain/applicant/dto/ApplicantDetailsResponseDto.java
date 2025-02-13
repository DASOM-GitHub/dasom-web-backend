package dmu.dasom.api.domain.applicant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "ApplicantDetailsResponseDto", description = "지원자 상세 응답 DTO")
public class ApplicantDetailsResponseDto extends ApplicantResponseDto {

    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "학년", example = "3")
    private int grade;

    @Schema(description = "지원 동기", example = "동아리 활동을 통해 새로운 경험을 쌓고 싶어서 지원합니다.")
    private String reasonForApply;

    @Schema(description = "희망 활동", example = "프로젝트")
    private String activityWish;

    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    private Boolean isPrivacyPolicyAgreed;

    @Schema(description = "지원일시", example = "2021-10-01T00:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2021-10-01T00:00:00")
    private LocalDateTime updatedAt;

}
