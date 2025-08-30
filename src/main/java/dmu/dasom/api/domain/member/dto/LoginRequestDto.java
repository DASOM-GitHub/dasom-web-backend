package dmu.dasom.api.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(name = "LoginRequestDto", description = "로그인 요청 DTO")
public class LoginRequestDto {

    @NotNull(message = "이메일은 필수 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @Size(max = 64)
    @Schema(description = "이메일 주소", example = "test@example.com")
    private String email;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Schema(description = "비밀번호", example = "password123!")
    private String password;
}
