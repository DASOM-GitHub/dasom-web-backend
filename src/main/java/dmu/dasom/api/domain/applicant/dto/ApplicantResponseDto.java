package dmu.dasom.api.domain.applicant.dto;

import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(name = "ApplicantResponseDto", description = "지원자 응답 DTO")
public class ApplicantResponseDto {

    @Schema(description = "id", example = "1")
    private Long id;

    @Schema(description = "학번", example = "20210000")
    private String studentNo;

    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "학년", example = "3")
    private int grade;

    @Schema(description = "지원 동기", example = "부원 지원 동기")
    private String reasonForApply;

    @Schema(description = "희망 활동", example = "스터디")
    private String activityWish;

    @Schema(description = "상태", example = "PENDING")
    private ApplicantStatus status;

    @Schema(description = "생성일", example = "2025-02-10 22:42:21.871801")
    private LocalDateTime createdAt;

}
