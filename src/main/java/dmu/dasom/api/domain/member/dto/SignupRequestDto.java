package dmu.dasom.api.domain.member.dto;

import dmu.dasom.api.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Schema(name = "SignupRequestDto", description = "회원가입 요청 DTO")
public class SignupRequestDto {

    @Email
    @Length(max = 64)
    @NotNull
    @Schema(description = "이메일", example = "test@example.com", maxLength = 64)
    private String email;

    @Length(min = 8, max = 128)
    @NotNull
    @Schema(description = "비밀번호", example = "password", minLength = 8, maxLength = 128)
    private String password;

    public Member toEntity(final String password) {
        return Member.builder()
                .email(this.email)
                .password(password)
                .build();
    }
}
