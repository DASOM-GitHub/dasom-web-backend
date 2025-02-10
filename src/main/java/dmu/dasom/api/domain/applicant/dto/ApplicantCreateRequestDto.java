package dmu.dasom.api.domain.applicant.dto;

import dmu.dasom.api.domain.applicant.entity.Applicant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
@Schema(name = "ApplicantCreateRequestDto", description = "지원자 생성 요청 DTO")
public class ApplicantCreateRequestDto {

    @NotNull
    @Pattern(regexp = "^[0-9]{8}$")
    @Size(min = 8, max = 8)
    @Schema(description = "학번", example = "20231234")
    private String studentNo;

    @NotNull
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @Size(min = 13, max = 13)
    @Schema(description = "연락처", example = "010-1234-5678")
    private String contact;

    @Email
    @NotNull
    @Size(max = 64)
    @Schema(description = "이메일 주소", example = "example@example.com")
    private String email;

    @Min(value = 1)
    @Max(value = 4)
    @NotNull
    @Schema(description = "학년", example = "3")
    private Integer grade;

    @NotNull
    @Size(max = 500)
    @Schema(description = "지원 동기", example = "동아리 활동에 관심이 있어서 지원합니다.")
    private String reasonForApply;

    @Size(max = 200)
    @Schema(description = "활동 희망사항", example = "동아리 활동 참여")
    private String activityWish;

    @NotNull
    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    private Boolean isPrivacyPolicyAgreed;

    public Applicant toEntity() {
        return Applicant.builder()
                .studentNo(this.studentNo)
                .contact(this.contact)
                .email(this.email)
                .grade(this.grade)
                .reasonForApply(this.reasonForApply)
                .activityWish(this.activityWish)
                .isPrivacyPolicyAgreed(this.isPrivacyPolicyAgreed)
                .build();
    }

}
