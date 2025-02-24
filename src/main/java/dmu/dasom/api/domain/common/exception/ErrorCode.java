package dmu.dasom.api.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    UNAUTHORIZED(401, "C001", "인증되지 않은 사용자입니다."),
    FORBIDDEN(403, "C002", "권한이 없습니다."),
    MEMBER_NOT_FOUND(400, "C003", "해당 회원을 찾을 수 없습니다."),
    TOKEN_EXPIRED(400, "C004", "토큰이 만료되었습니다."),
    LOGIN_FAILED(400, "C005", "로그인에 실패하였습니다."),
    SIGNUP_FAILED(400, "C006", "회원가입에 실패하였습니다."),
    ARGUMENT_NOT_VALID(400, "C007", "요청한 값이 올바르지 않습니다."),
    TOKEN_NOT_VALID(400, "C008", "토큰이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(500, "C009", "서버에 문제가 발생하였습니다."),
    NOT_FOUND(404, "C010", "해당 리소스를 찾을 수 없습니다."),
    WRITE_FAIL(400, "C011", "데이터를 쓰는데 실패하였습니다."),
    EMPTY_RESULT(400, "C012", "조회 결과가 없습니다."),
    DUPLICATED_STUDENT_NO(400, "C013", "이미 등록된 학번입니다."),
    SEND_EMAIL_FAIL(400, "C014", "이메일 전송에 실패하였습니다."),
    MAIL_TYPE_NOT_VALID(400, "C015", "메일 타입이 올바르지 않습니다."),
    INVALID_DATETIME_FORMAT(400, "C016", "날짜 형식이 올바르지 않습니다."),
    INVALID_TIME_FORMAT(400, "C017", "시간 형식이 올바르지 않습니다."),

    private final int status;
    private final String code;
    private final String message;

}
