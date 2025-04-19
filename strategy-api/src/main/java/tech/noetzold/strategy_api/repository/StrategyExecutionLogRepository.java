package tech.noetzold.strategy_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.noetzold.strategy_api.model.StrategyExecutionLog;

public interface StrategyExecutionLogRepository extends JpaRepository<StrategyExecutionLog, Long> {

    Page<StrategyExecutionLog> findByUserIdAndStrategyNameOrderByTimestampDesc(Long userId, String strategyName, Pageable pageable);
    Page<StrategyExecutionLog> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

}
