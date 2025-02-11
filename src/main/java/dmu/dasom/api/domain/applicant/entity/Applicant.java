package dmu.dasom.api.domain.applicant.entity;

import dmu.dasom.api.domain.applicant.dto.ApplicantResponseDto;
import dmu.dasom.api.domain.applicant.enums.ApplicantStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Applicant {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "student_no", nullable = false, length = 8)
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

    public ApplicantResponseDto toApplicantResponse() {
        return ApplicantResponseDto.builder()
                .id(this.id)
                .studentNo(this.studentNo)
                .contact(this.contact)
                .email(this.email)
                .grade(this.grade)
                .reasonForApply(this.reasonForApply)
                .activityWish(this.activityWish)
                .status(this.status)
                .createdAt(this.createdAt)
                .build();
    }

}
