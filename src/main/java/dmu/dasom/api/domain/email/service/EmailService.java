package dmu.dasom.api.domain.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
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

    public void sendEmail(String to, String subject, String name) throws MessagingException {
        // HTML 템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariable("name", name); // 지원자 이름 전달

        // HTML 템플릿 처리
        String htmlBody = templateEngine.process("email-template", context);

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
