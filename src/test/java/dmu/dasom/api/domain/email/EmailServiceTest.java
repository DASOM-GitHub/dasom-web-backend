package dmu.dasom.api.domain.email;

import dmu.dasom.api.domain.common.exception.CustomException;
import dmu.dasom.api.domain.common.exception.ErrorCode;
import dmu.dasom.api.domain.email.enums.MailType;
import dmu.dasom.api.domain.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // 테스트 환경에서 from 값을 설정
        ReflectionTestUtils.setField(emailService, "from", "test_email@example.com");
    }

    @Test
    @DisplayName("서류 합격 메일 발송 테스트")
    void sendEmail_documentResult() throws MessagingException {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailType mailType = MailType.DOCUMENT_RESULT;

        String expectedTemplate = "document-pass-template";
        String expectedHtmlBody = "<html><body>Document Pass</body></html>";
        when(templateEngine.process(eq(expectedTemplate), any(Context.class))).thenReturn(expectedHtmlBody);

        // when
        emailService.sendEmail(to, name, mailType);

        // then
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        verify(templateEngine).process(eq(expectedTemplate), any(Context.class));
    }

    @Test
    @DisplayName("최종 합격 메일 발송 테스트")
    void sendEmail_finalResult() throws MessagingException {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailType mailType = MailType.FINAL_RESULT;

        String expectedTemplate = "final-pass-template";
        String expectedHtmlBody = "<html><body>Final Pass</body></html>";
        when(templateEngine.process(eq(expectedTemplate), any(Context.class))).thenReturn(expectedHtmlBody);

        // when
        emailService.sendEmail(to, name, mailType);

        // then
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        MimeMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        verify(templateEngine).process(eq(expectedTemplate), any(Context.class));
    }

    @Test
    @DisplayName("잘못된 MailType 처리 테스트")
    void sendEmail_invalidMailType() {
        // given
        String to = "applicant@example.com";
        String name = "지원자";

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            emailService.sendEmail(to, name, null);
        });

        assertEquals(ErrorCode.MAIL_TYPE_NOT_VALID, exception.getErrorCode());
    }
}