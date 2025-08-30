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
        MailSendStatus mailSendStatus = MailSendStatus.SUCCESS;
        String errorMessage = null;
        try {
            if (mailType == null) {
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
            }

            // 메일 템플릿 조회
            MailTemplate mailTemplate = MailTemplate.getMailType(mailType);
            String buttonUrl = "https://dmu-dasom.or.kr/recruit/result";

            // HTML 템플릿에 전달할 데이터 설정
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("buttonUrl", buttonUrl);

            // HTML 템플릿 처리
            String htmlBody = templateEngine.process(mailTemplate.getTemplateName(), context);

            // 이메일 생성 및 전송
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(mailTemplate.getSubject());
            helper.setText(htmlBody, true);
            helper.setFrom((from != null && !from.isEmpty()) ? from : "dasomdmu@gmail.com");

            message.setContent(htmlBody, "text/html; charset=utf-8");

            javaMailSender.send(message);
            log.info("Email sent successfully {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            mailSendStatus = MailSendStatus.FAILURE;
            errorMessage = e.getMessage();
        } catch (CustomException e) {
            log.error("Email sending error for {}: {}", to, e.getMessage());
            mailSendStatus = MailSendStatus.FAILURE;
            errorMessage = e.getMessage();
        }
        emailLogService.logEmailSending(to, mailSendStatus, errorMessage);
    }

    /*
     * 면접 예약 변경을 위한 인증코드 발송
     * - VerificationCodeManager에서 생성된 코드를 이메일로 전송
     * - verify-num-email.html 템플릿을 이용해 코드와 버튼 링크 포함
     */
    public void sendVerificationEmail(String to, String name, String code) throws MessagingException {
        String subject = "DASOM 면접 시간 변경을 위한 이메일 인증 코드 안내";

        // 인증 코드만 템플릿으로 전달
        String emailContent = "인증 코드: <strong>" + code + "</strong>";

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("emailContent", emailContent);
        context.setVariable("buttonUrl", "https://dmu-dasom.or.kr");
        context.setVariable("buttonText", "인증 완료");

        String htmlBody = templateEngine.process("verify-num-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom((from != null && !from.isEmpty()) ? from : "dasomdmu@gmail.com");

        javaMailSender.send(message);
    }

}
