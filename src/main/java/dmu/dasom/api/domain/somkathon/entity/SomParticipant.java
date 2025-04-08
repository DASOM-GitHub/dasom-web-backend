package dmu.dasom.api.domain.somkathon.entity;

import dmu.dasom.api.domain.common.BaseEntity;
import dmu.dasom.api.domain.somkathon.dto.SomParticipantRequestDto;
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

    public void update(SomParticipantRequestDto requestDto) {
        this.participantName = requestDto.getParticipantName();
        this.studentId = requestDto.getStudentId();
        this.department = requestDto.getDepartment();
        this.grade = requestDto.getGrade();
        this.contact = requestDto.getContact();
        this.email = requestDto.getEmail();
    }
}
