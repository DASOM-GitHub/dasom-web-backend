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
    INVALID_INQUIRY_PERIOD(400, "C018", "조회 기간이 아닙니다."),
    SHEET_WRITE_FAIL(400, "C019", "시트에 데이터를 쓰는데 실패하였습니다."),
    SHEET_READ_FAIL(400, "C200", "시트에 데이터를 읽는데 실패하였습니다."),
    SLOT_NOT_FOUND(400, "C021", "슬롯을 찾을 수 없습니다."),
    APPLICANT_NOT_FOUND(400, "C022", "지원자를 찾을 수 없습니다."),
    ALREADY_RESERVED(400, "C023", "이미 예약된 지원자입니다."),
    RESERVED_SLOT_CANNOT_BE_DELETED(400, "C024", "예약된 슬롯은 삭제할 수 없습니다."),
    SLOT_FULL(400, "C025", "해당 슬롯이 가득 찼습니다."),
    RESERVATION_NOT_FOUND(400, "C026", "예약을 찾을 수 없습니다."),
    SLOT_NOT_ACTIVE(400, "C027", "해당 슬롯이 비활성화 되었습니다."),
    FILE_UPLOAD_FAIL(400, "C028", "파일 업로드에 실패하였습니다."),
    RECRUITMENT_NOT_ACTIVE(400, "C029", "모집 기간이 아닙니다."),
    NOT_FOUND_PARTICIPANT(400, "C030", "참가자를 찾을 수 없습니다."),
    EXECUTIVE_NOT_FOUND(400, "C031", "임원진을 찾을 수 없습니다."),
    GENERATION_NOT_FOUND(400, "C032", "저장된 기수를 찾을 수 없습니다."),
    INVALID_GENERATION_FORMAT(400, "C033", "유효하지 않은 기수 형식입니다. (예: '1기')");

    private final int status;
    private final String code;
    private final String message;

}
