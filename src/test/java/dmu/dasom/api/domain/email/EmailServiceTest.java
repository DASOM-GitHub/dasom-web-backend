package dmu.dasom.api.domain.email;


import dmu.dasom.api.domain.google.enums.MailType;
import dmu.dasom.api.domain.google.service.EmailService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private TemplateEngine templateEngine;
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        ReflectionTestUtils.setField(emailService, "from", "test_email@example.com");
    }

    @Test
    @DisplayName("성공 - 서류 결과 메일 발송 테스트")
    void sendDocumentResultMessage_Success() throws MessagingException {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailType mailType = MailType.DOCUMENT_RESULT;

        String expectedHtmlBody = "<html><body>Test HTML</body></html>";
        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn(expectedHtmlBody);

        // when
        emailService.sendEmail(to, name, mailType);

        //then
        // 비동기 처리 확인
        verify(javaMailSender, timeout(1000)).send(any(MimeMessage.class));
        // 메일 전송 확인
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email-template"), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();

        // context 변수 확인
        assertEquals(name, capturedContext.getVariable("name"));
        assertEquals("서류 결과 확인하기", capturedContext.getVariable("buttonText"));
        assertNotNull(capturedContext.getVariable("emailContent"));
    }

    @Test
    @DisplayName("성공 - 최종 결과 메일 발송 테스트")
    void sendFinalResultMessage_Success() throws MessagingException {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailType mailType = MailType.FINAL_RESULT;

        String expectedHtmlBody = "<html><body>Test HTML</body></html>";
        when(templateEngine.process(eq("email-template"), any(Context.class))).thenReturn(expectedHtmlBody);

        // when
        emailService.sendEmail(to, name, mailType);

        //then
        // 비동기 처리 확인
        verify(javaMailSender, timeout(1000)).send(any(MimeMessage.class));
        // 메일 전송 확인
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq("email-template"), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();

        // context 변수 확인
        assertEquals(name, capturedContext.getVariable("name"));
        assertEquals("최종 결과 확인하기", capturedContext.getVariable("buttonText"));
        assertNotNull(capturedContext.getVariable("emailContent"));
    }

    @Test
    @DisplayName("실패 - MailType이 null일 경우, 예외 발생 테스트")
    void sendEmail_nullMailType_shouldNotSend() {
        //given
        String to = "applicant@example.com";
        String name = "지원자";

        // when
        emailService.sendEmail(to, name, null);

        // then
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }


}