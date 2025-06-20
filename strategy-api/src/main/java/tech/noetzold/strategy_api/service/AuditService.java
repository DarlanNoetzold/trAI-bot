package tech.noetzold.strategy_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.noetzold.strategy_api.model.AuditLog;
import tech.noetzold.strategy_api.repository.AuditLogRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(Long userId, String email, String action, String resource, String description) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setEmail(email);
        log.setAction(action);
        log.setResource(resource);
        log.setDescription(description);
        log.setTimestamp(Instant.now());
        auditLogRepository.save(log);
    }
}
