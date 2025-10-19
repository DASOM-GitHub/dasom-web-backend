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

    public void update(SomParticipantRequestDto requestDto) {
        this.participantName = requestDto.getParticipantName();
        this.studentId = requestDto.getStudentId();
        this.department = requestDto.getDepartment();
        this.grade = requestDto.getGrade();
        this.contact = requestDto.getContact();
        this.email = requestDto.getEmail();
        this.githubLink = requestDto.getGithubLink();
        this.portfolioLink = requestDto.getPortfolioLink();
    }

    /**
     * Entity → Response DTO 변환 메서드
     */
    public SomParticipantResponseDto toResponseDto(SomParticipant participant) {
        return SomParticipantResponseDto.builder()
                .id(participant.getId())
                .participantName(participant.getParticipantName())
                .studentId(participant.getStudentId())
                .department(participant.getDepartment())
                .grade(participant.getGrade())
                .contact(participant.getContact())
                .email(participant.getEmail())
                .githubLink(participant.getGithubLink())
                .portfolioLink(participant.getPortfolioLink())
                .build();
    }
}
