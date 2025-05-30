package tech.noetzold.notification_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.notification_api.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
