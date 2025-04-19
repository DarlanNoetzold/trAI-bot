package tech.noetzold.spot_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.noetzold.spot_api.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
