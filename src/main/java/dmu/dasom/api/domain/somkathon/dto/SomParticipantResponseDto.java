package dmu.dasom.api.domain.somkathon.dto;

import dmu.dasom.api.domain.somkathon.entity.SomParticipant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SomParticipantResponseDto {
    private Long id;                // 참가자 ID
    private String participantName; // 참가자 이름
    private String studentId;       // 학번
    private String department;      // 학과
    private String grade;           // 학년
    private String contact;         // 연락처
    private String email;           // 이메일


}
