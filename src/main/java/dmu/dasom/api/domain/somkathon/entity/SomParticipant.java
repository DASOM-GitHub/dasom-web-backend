package dmu.dasom.api.domain.somkathon.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantRequestDto;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class SomParticipant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String participantName; // 참가자 이름

    @Column(nullable = false, unique = true)
    private String studentId; // 학번

    @Column(nullable = false)
    private String department; // 학과

    @Column(nullable = false)
    private String grade; // 학년

    @Column(nullable = false)
    private String contact; // 연락처

    @Column(nullable = false)
    private String email; // 이메일

    @Column
    private String githubLink; // 깃허브 링크

    @Column(nullable = false)
    private String portfolioLink; // 포트폴리오 링크

    @Column
    private Boolean isTransferredInCS; // 컴퓨터공학부 내 전과 여부

    @Column
    private Boolean isPaid; // 학생회비 납부 여부

    public void update(SomParticipantRequestDto requestDto) {
        this.participantName = requestDto.getParticipantName();
        this.studentId = requestDto.getStudentId();
        this.department = requestDto.getDepartment();
        this.grade = requestDto.getGrade();
        this.contact = requestDto.getContact();
        this.email = requestDto.getEmail();
        this.githubLink = requestDto.getGithubLink();
        this.portfolioLink = requestDto.getPortfolioLink();
        this.isTransferredInCS = requestDto.getIsTransferredInCS();
        this.isPaid = requestDto.getIsPaid();
    }

    /**
     * Entity → Response DTO 변환 메서드
     */
    public SomParticipantResponseDto toResponseDto() {
        return SomParticipantResponseDto.builder()
                .id(this.getId())
                .participantName(this.getParticipantName())
                .studentId(this.getStudentId())
                .department(this.getDepartment())
                .grade(this.getGrade())
                .contact(this.getContact())
                .email(this.getEmail())
                .githubLink(this.getGithubLink())
                .portfolioLink(this.getPortfolioLink())
                .isTransferredInCS(this.getIsTransferredInCS())
                .isPaid(this.getIsPaid())
                .build();
    }
}