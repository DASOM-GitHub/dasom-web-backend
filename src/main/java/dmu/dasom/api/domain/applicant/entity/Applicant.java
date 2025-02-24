package dmu.dasom.api.domain.applicant.entity;

import dmu.dasom.api.domain.applicant.dto.ApplicantCreateRequestDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantDetailsResponseDto;
import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@DynamicUpdate
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Applicant {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 16)
    @Size(max = 16)
    private String name;

    @Column(name = "student_no", nullable = false, unique = true, length = 8)
    @Pattern(regexp = "^[0-9]{8}$")
    @Size(min = 8, max = 8)
    private String studentNo;

    @Column(name = "contact", nullable = false, length = 13)
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$")
    @Size(min = 13, max = 13)
    private String contact;

    @Column(name = "email", nullable = false, length = 64)
    @Email
    @Size(max = 64)
    private String email;

    @Column(name = "grade", nullable = false)
    @Min(1) @Max(4)
    private int grade;

    @Column(name = "reason_for_apply", nullable = false, length = 500)
    @Size(max = 500)
    private String reasonForApply;

    @Column(name = "activity_wish", nullable = true, length = 200)
    @Size(max = 200)
    private String activityWish;

    @Column(name = "is_privacy_policy_agreed", nullable = false)
    private Boolean isPrivacyPolicyAgreed;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private ApplicantStatus status = ApplicantStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void updateStatus(final ApplicantStatus status) {
        this.status = status;
    }

    public List<Object> toGoogleSheetRow(){
        return List.of(
                this.id,
                this.name,
                this.studentNo,
                this.contact,
                this.email,
                this.grade,
                this.reasonForApply,
                this.activityWish,
                this.isPrivacyPolicyAgreed,
                this.status.name(),
                this.createdAt.toString(),
                this.updatedAt.toString()
        );
    }

    public ApplicantResponseDto toApplicantResponse() {
        return ApplicantResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .studentNo(this.studentNo)
                .status(this.status)
                .build();
    }

    public ApplicantDetailsResponseDto toApplicantDetailsResponse() {
        return ApplicantDetailsResponseDto.builder()
                .id(this.id)
                .name(this.name)
                .studentNo(this.studentNo)
                .contact(this.contact)
                .email(this.email)
                .grade(this.grade)
                .reasonForApply(this.reasonForApply)
                .activityWish(this.activityWish)
                .isPrivacyPolicyAgreed(this.isPrivacyPolicyAgreed)
                .status(this.status)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

    public void overwrite(final ApplicantCreateRequestDto request) {
        this.name = request.getName();
        this.contact = request.getContact();
        this.email = request.getEmail();
        this.grade = request.getGrade();
        this.reasonForApply = request.getReasonForApply();
        this.activityWish = request.getActivityWish();
        this.isPrivacyPolicyAgreed = request.getIsPrivacyPolicyAgreed();
    }

}
