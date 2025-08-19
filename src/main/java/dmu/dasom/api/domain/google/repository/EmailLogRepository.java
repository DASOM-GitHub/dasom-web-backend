package dmu.dasom.api.domain.google.repository;

import dmu.dasom.api.domain.google.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
}
