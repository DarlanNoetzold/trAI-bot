package tech.noetzold.spot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.spot_api.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}