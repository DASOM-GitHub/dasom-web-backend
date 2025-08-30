package dmu.dasom.api.domain.google.enums;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MailTemplate {
    DOCUMENT_RESULT(MailType.DOCUMENT_RESULT, "동양미래대학교 컴퓨터소프트웨어공학과 전공 동아리 DASOM 서류 결과 안내", "document-result-email"),
    FINAL_RESULT(MailType.FINAL_RESULT, "동양미래대학교 컴퓨터소프트웨어공학과 전공 동아리 DASOM 최종 면접 결과 안내", "final-result-email");

    private final MailType mailType;
    private final String subject;
    private final String templateName;

    public static MailTemplate getMailType(MailType mailType) {
        return Arrays.stream(values())
                .filter(template -> template.getMailType() == mailType)
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID));
    }
}
