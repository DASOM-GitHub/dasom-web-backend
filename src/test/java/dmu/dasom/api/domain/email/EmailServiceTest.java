package dmu.dasom.api.domain.email;


import dmu.dasom.api.domain.google.enums.MailTemplate;
import dmu.dasom.api.domain.google.enums.MailType;
import dmu.dasom.api.domain.google.service.EmailLogService;
import dmu.dasom.api.domain.google.service.EmailService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private TemplateEngine templateEngine;
    @Mock
    private EmailLogService emailLogService;
    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        doNothing().when(mimeMessage).setSubject(anyString(), anyString());
        ReflectionTestUtils.setField(emailService, "from", "test_email@example.com");
    }

    private void testSendEmailSuccess(MailType mailType) throws Exception {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailTemplate expectedTemplate = MailTemplate.getMailType(mailType);
        String expectedHtmlBody = "<html><body>Test HTML for " + mailType + "</body></html>";

        when(templateEngine.process(eq(expectedTemplate.getTemplateName()), any(Context.class))).thenReturn(expectedHtmlBody);

        //when
        emailService.sendEmail(to, name, mailType);

        // then
        // 비동기 처리를 위해 잠시 대기 후 검증
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(javaMailSender, timeout(1000)).send(messageCaptor.capture());

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq(expectedTemplate.getTemplateName()), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();

        assertEquals(name, capturedContext.getVariable("name"));
        assertEquals("https://dmu-dasom.or.kr/recruit/result", capturedContext.getVariable("buttonUrl"));

        MimeMessage capturedMessage = messageCaptor.getValue();
        verify(capturedMessage).setSubject(expectedTemplate.getSubject(), "UTF-8");
    }

    @Test
    @DisplayName("성공 - 서류 결과 메일 발송 테스트")
    void sendDocumentResultMessage_Success() throws Exception {
        testSendEmailSuccess(MailType.DOCUMENT_RESULT);
    }

    @Test
    @DisplayName("성공 - 최종 결과 메일 발송 테스트")
    void sendFinalResultMessage_Success() throws Exception {
        testSendEmailSuccess(MailType.FINAL_RESULT);
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
        verify(emailLogService, timeout(1000)).logEmailSending(eq(to), any(), any());
    }
}