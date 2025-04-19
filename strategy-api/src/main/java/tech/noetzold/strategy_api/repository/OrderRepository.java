package tech.noetzold.strategy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.strategy_api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
