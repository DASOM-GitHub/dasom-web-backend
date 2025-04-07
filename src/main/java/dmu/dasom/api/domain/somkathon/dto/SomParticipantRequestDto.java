package dmu.dasom.api.domain.somkathon.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SomParticipantRequestDto {
    private String participantName; // 참가자 이름
    private String studentId;
    private String department; // 학과
    private String grade; // 학년
    private String contact; // 연락처
    private String email; // 이메일
}
