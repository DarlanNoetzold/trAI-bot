package tech.noetzold.spot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.spot_api.model.CustomStrategy;

import java.util.Optional;

@Repository
public interface CustomStrategyRepository extends JpaRepository<CustomStrategy, Long> {
    Optional<CustomStrategy> findByName(String strategyName);
}
