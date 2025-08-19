package dmu.dasom.api.domain.google.entity;

import dmu.dasom.api.domain.google.enums.MailSendStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MailSendStatus status;

    private String errorMessage;

    @CreationTimestamp
    private LocalDateTime sentAt;

    public static EmailLog of(String recipientEmail, MailSendStatus status, String errorMessage) {
        EmailLog emailLog = new EmailLog();
        emailLog.recipientEmail = recipientEmail;
        emailLog.status = status;
        emailLog.errorMessage = errorMessage;
        return emailLog;
    }

}
