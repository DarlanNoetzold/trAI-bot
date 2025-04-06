package tech.noetzold.crypto_bot_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.crypto_bot_backend.model.Strategy;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {
}
