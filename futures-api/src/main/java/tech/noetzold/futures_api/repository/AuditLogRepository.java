package tech.noetzold.futures_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.futures_api.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}