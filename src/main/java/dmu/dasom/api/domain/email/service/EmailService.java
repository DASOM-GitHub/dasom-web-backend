package dmu.dasom.api.domain.email.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.email.enums.MailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
public class EmailService {

    private JavaMailSender javaMailSender;
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(String to, String name, MailType mailType) throws MessagingException {
        if (mailType == null){
            throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
        }
        // 메일 제목 및 템플릿 설정
        String subject;
        String templateName = switch (mailType) {
            case DOCUMENT_RESULT -> {
                subject = "서류 합격 안내";
                yield "document-pass-template";
            }
            case FINAL_RESULT -> {
                subject = "최종 합격 안내";
                yield "final-pass-template";
            }
            default -> throw new IllegalStateException("Unexpected value: " + mailType);
        };

        // HTML 템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("name", name); // 지원자 이름 전달

        // HTML 템플릿 처리
        String htmlBody = templateEngine.process(templateName, context);

        // 이메일 생성 및 전송
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom(from);

        javaMailSender.send(message);
    }

}
