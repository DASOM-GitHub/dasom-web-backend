package dmu.dasom.api.global.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {

    @Email
    @NotNull
    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @NotNull
    @Schema(description = "비밀번호", example = "password")
    private String password;

}
