package dmu.dasom.api.domain.somkathon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "솜커톤 참가자 요청 DTO")
public class SomParticipantRequestDto {

    @NotBlank(message = "참가자 이름은 필수 입력 값입니다.")
    @Size(max = 50, message = "참가자 이름은 최대 50자까지 입력 가능합니다.")
    @Schema(description = "참가자 이름", example = "유승완", required = true)
    private final String participantName; // 참가자 이름

    @NotBlank(message = "학번은 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9]{8}$", message = "학번은 8자리 숫자여야 합니다.")
    @Schema(description = "학번 (8자리 숫자)", example = "20250001", required = true)
    private final String studentId;

    @NotBlank(message = "학과는 필수 입력 값입니다.")
    @Size(max = 100, message = "학과는 최대 100자까지 입력 가능합니다.")
    @Schema(description = "학과", example = "컴퓨터소프트웨어공학과", required = true)
    private final String department; // 학과

    @NotBlank(message = "학년은 필수 입력 값입니다.")
    @Pattern(regexp = "^[1-4]$", message = "학년은 1~4 사이의 숫자여야 합니다.")
    @Schema(description = "학년 (1~4)", example = "3", required = true)
    private final String grade; // 학년

    @NotBlank(message = "연락처는 필수 입력 값입니다.")
    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "연락처는 '010-XXXX-XXXX' 형식이어야 합니다.")
    @Schema(description = "연락처 (010-XXXX-XXXX 형식)", example = "010-1234-5678", required = true)
    private final String contact; // 연락처

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Schema(description = "이메일 주소", example = "hong@example.com", required = true)
    private final String email; // 이메일

    @URL(protocol = "https", host = "github.com", message = "GitHub URL이 올바르지 않습니다.")
    @Schema(description = "GitHub 주소", example = "https://github.com/username")
    private final String githubLink; // 깃허브 주소

    @NotBlank(message = "포트폴리오 주소는 필수 입력 값입니다.")
    @URL(protocol = "https", message = "포트폴리오 URL이 올바르지 않습니다.")
    @Schema(description = "포트폴리오 주소", example = "https://portfolio.com/username", required = true)
    private final String portfolioLink; // 포트폴리오 주소
}
