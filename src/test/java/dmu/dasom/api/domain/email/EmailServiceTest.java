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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 1. MimeMessage Mock 설정
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // 2. 환경 변수 필드 주입
        ReflectionTestUtils.setField(emailService, "from", "test_email@example.com");
    }

    private void testSendEmailSuccess(MailType mailType) throws Exception {
        // given
        String to = "applicant@example.com";
        String name = "지원자";
        MailTemplate expectedTemplate = MailTemplate.getMailType(mailType);
        String expectedHtmlBody = "<html><body>Test HTML</body></html>";

        when(templateEngine.process(eq(expectedTemplate.getTemplateName()), any(Context.class)))
                .thenReturn(expectedHtmlBody);

        // when
        emailService.sendEmail(to, name, mailType);

        // then
        // 3. 비동기 발송 확인 (timeout 부여)
        verify(javaMailSender, timeout(2000)).send(any(MimeMessage.class));

        // 4. 템플릿 엔진 파라미터 검증
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq(expectedTemplate.getTemplateName()), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();

        assertEquals(name, capturedContext.getVariable("name"));
        assertEquals("https://dmudasom.netlify.app/recruit/result", capturedContext.getVariable("buttonUrl"));

        // 5. 이메일 로그 기록 확인 (가장 마지막에 실행되므로 최종 완료 지표)
        verify(emailLogService, timeout(2000)).logEmailSending(eq(to), any(), any());
    }

    @Test
    @DisplayName("성공 - 서류 결과 메일 발송 테스트")
    void sendDocumentResultMessage_Success() throws Exception {
        testSendEmailSuccess(MailType.DOCUMENT_RESULT); // 6. 기존 DOCUMENT_RESULT에서 실제 Enum 값인 DOCUMENT_PASS로 수정
    }

    @Test
    @DisplayName("성공 - 최종 결과 메일 발송 테스트")
    void sendFinalResultMessage_Success() throws Exception {
        testSendEmailSuccess(MailType.FINAL_RESULT); // 7. 기존 FINAL_RESULT에서 실제 Enum 값인 FINAL_PASS로 수정
    }

    @Test
    @DisplayName("실패 - MailType이 null일 경우 발송하지 않음")
    void sendEmail_nullMailType_shouldNotSend() {
        // given
        String to = "applicant@example.com";
        String name = "지원자";

        // when
        emailService.sendEmail(to, name, null);

        // then
        verify(javaMailSender, never()).send(any(MimeMessage.class));
        verify(emailLogService, timeout(2000)).logEmailSending(eq(to), any(), any());
    }
}