package dmu.dasom.api.domain.google.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.google.enums.MailSendStatus;
import dmu.dasom.api.domain.google.enums.MailTemplate;
import dmu.dasom.api.domain.google.enums.MailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final EmailLogService emailLogService;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(String to, String name, MailType mailType) {
        try {
            if (mailType == null) {
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
            }

            // 메일 템플릿 조회
            MailTemplate mailTemplate = MailTemplate.getMailType(mailType);
            String buttonUrl = "https://dmu-dasom.or.kr/recruit/result";

            // HTML 템플릿에 전달할 데이터 설정
            Context context = new Context();
            context.setVariable("name", name); // 지원자 이름 전달
            context.setVariable("buttonUrl", buttonUrl); // 버튼 링크 전달


            // HTML 템플릿 처리
            String htmlBody = templateEngine.process(mailTemplate.getTemplateName(), context);

            // 이메일 생성 및 전송
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(htmlBody, true);
            helper.setFrom((from != null && !from.isEmpty()) ? from : "dasomdmu@gmail.com");

            // Content-Type을 명시적으로 설정
            message.setContent(htmlBody, "text/html; charset=utf-8");

            javaMailSender.send(message);
            log.info("Email sent successfull {}", to);
            emailLogService.logEmailSending(to, MailSendStatus.SUCCESS, null);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            emailLogService.logEmailSending(to, MailSendStatus.FAILURE, e.getMessage());
        } catch (CustomException e) {
            log.error("Email sending error for {}: {}", to, e.getMessage());
            emailLogService.logEmailSending(to, MailSendStatus.FAILURE, e.getMessage());
        }
    }
}
