package dmu.dasom.api.domain.google.service;

import dmu.dasom.api.domain.google.entity.EmailLog;
import dmu.dasom.api.domain.google.enums.MailSendStatus;
import dmu.dasom.api.domain.google.repository.EmailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailLogService {

    private final EmailLogRepository emailLogRepository;

    @Async
    public void logEmailSending(String recipientEmail, MailSendStatus status, String errorMessage) {
        EmailLog emailLog = EmailLog.of(recipientEmail, status, errorMessage);
        emailLogRepository.save(emailLog);
    }
}
