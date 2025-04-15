package tech.noetzold.crypto_bot_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.crypto_bot_backend.model.CustomStrategy;

@Repository
public interface CustomStrategyRepository extends JpaRepository<CustomStrategy, Long> {
}
