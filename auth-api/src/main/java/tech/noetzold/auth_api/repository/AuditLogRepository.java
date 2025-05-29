package tech.noetzold.auth_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.auth_api.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}