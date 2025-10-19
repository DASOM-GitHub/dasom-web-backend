package dmu.dasom.api.domain.somkathon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "솜커톤 참가자 응답 DTO")
public class SomParticipantResponseDto {

    @Schema(description = "참가자 ID", example = "1", required = true)
    private Long id;                // 참가자 ID

    @Schema(description = "참가자 이름", example = "홍길동", required = true)
    private String participantName; // 참가자 이름

    @Schema(description = "학번 (8자리 숫자)", example = "20230001", required = true)
    private String studentId;       // 학번

    @Schema(description = "학과", example = "컴퓨터공학과", required = true)
    private String department;      // 학과

    @Schema(description = "학년 (1~4)", example = "3", required = true)
    private String grade;           // 학년

    @Schema(description = "연락처 (010-XXXX-XXXX 형식)", example = "010-1234-5678", required = true)
    private String contact;         // 연락처

    @Schema(description = "이메일 주소", example = "hong@example.com", required = true)
    private String email;           // 이메일

    @Schema(description = "깃허브 주소", example = "https://github.com/username", required = true)
    private String githubLink; // 깃허브 주소

    @Schema(description = "포트폴리오 주소", example = "https://portfolio.com/username", required = true)
    private String portfolioLink; // 포트폴리오 주소
}