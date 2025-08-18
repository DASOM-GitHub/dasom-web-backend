package dmu.dasom.api.domain.google.service;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.google.enums.MailType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String from;

    @Async
    public void sendEmail(String to, String name, MailType mailType) {
        try {
            if (mailType == null) {
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
            }

            // 메일 제목 및 템플릿 설정
            String subject = getSubject(mailType);
            String emailContent = getEmailContent(mailType);
            String buttonText = getButtonText(mailType);
            String buttonUrl = "https://dmu-dasom.or.kr/recruit/result";

            // HTML 템플릿에 전달할 데이터 설정
            Context context = new Context();
            context.setVariable("name", name); // 지원자 이름 전달
            context.setVariable("emailContent", emailContent); // 이메일 내용 전달
            context.setVariable("buttonUrl", buttonUrl); // 버튼 링크 전달
            context.setVariable("buttonText", buttonText);

            // HTML 템플릿 처리
            String htmlBody = templateEngine.process("email-template", context);

            // 이메일 생성 및 전송
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom((from != null && !from.isEmpty()) ? from : "dasomdmu@gmail.com");

            // Content-Type을 명시적으로 설정
            message.setContent(htmlBody, "text/html; charset=utf-8");

            javaMailSender.send(message);
            log.info("Email sent successfull {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        } catch (CustomException e) {
            log.error("Email sending error for {}: {}", to, e.getMessage());
        }
    }

    // 메일 유형에 맞는 제목 반환
    private String getSubject(MailType mailType){
        switch (mailType){
            case DOCUMENT_RESULT:
                return "동양미래대학교 컴퓨터소프트웨어공학과 전공 동아리 DASOM 서류 결과 안내";
            case FINAL_RESULT:
                return "동양미래대학교 컴퓨터소프트웨어공학과 전공 동아리 DASOM 최종 면접 결과 안내";
            default:
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
        }
    }

    // 메일 유형에 맞는 본문 반환
    private String getEmailContent(MailType mailType){
        switch (mailType) {
            case DOCUMENT_RESULT:
                return "먼저 다솜 34기에 많은 관심을 두고 지원해 주셔서 감사드리며,<br>" +
                        "내부 서류 평가 결과 및 추후 일정에 관해 안내해드리고자 이메일을 발송하게 되었습니다.<br>" +
                        "서류 전형 결과는 아래 버튼 혹은 홈페이지를 통해 확인이 가능합니다.";
            case FINAL_RESULT:
                return "먼저 다솜 34기에 많은 관심을 두고 지원해 주셔서 감사드리며,<br>" +
                        "최종 면접 결과 및 추후 일정에 관해 안내해드리고자 이메일을 발송하게 되었습니다.<br>" +
                        "최종 면접 결과는 아래 버튼 혹은 홈페이지를 통해 확인이 가능합니다.";
            default:
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
        }
    }

    // 메일 유형에 맞는 버튼 텍스트 반환
    private String getButtonText(MailType mailType) {
        switch (mailType) {
            case DOCUMENT_RESULT:
                return "서류 결과 확인하기";
            case FINAL_RESULT:
                return "최종 결과 확인하기";
            default:
                throw new CustomException(ErrorCode.MAIL_TYPE_NOT_VALID);
        }
    }
}
