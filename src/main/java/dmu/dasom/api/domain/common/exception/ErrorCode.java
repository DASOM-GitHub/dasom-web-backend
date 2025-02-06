package dmu.dasom.api.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    UNAUTHORIZED(401, "C001", "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "C002", "권한이 없습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

}
