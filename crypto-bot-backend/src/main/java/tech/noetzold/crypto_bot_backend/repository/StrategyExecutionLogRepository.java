package tech.noetzold.crypto_bot_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.crypto_bot_backend.model.StrategyExecutionLog;

public interface StrategyExecutionLogRepository extends JpaRepository<StrategyExecutionLog, Long> {

    Page<StrategyExecutionLog> findByUserId(Long userId, Pageable pageable);

    Page<StrategyExecutionLog> findByUserIdAndStrategyName(Long userId, String strategyName, Pageable pageable);
}
