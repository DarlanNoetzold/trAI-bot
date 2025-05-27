package tech.noetzold.strategy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.strategy_api.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}