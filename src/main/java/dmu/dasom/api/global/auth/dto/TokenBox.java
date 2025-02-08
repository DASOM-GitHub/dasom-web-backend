package dmu.dasom.api.global.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "토큰 BOX")
public class TokenBox {

    @Schema(description = "AccessToken")
    @NotNull
    private String accessToken;

    @Schema(description = "RefreshToken")
    @NotNull
    private String refreshToken;

}
