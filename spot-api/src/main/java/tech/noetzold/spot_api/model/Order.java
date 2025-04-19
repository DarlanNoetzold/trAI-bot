package tech.noetzold.spot_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private String side;

    private String type;

    private BigDecimal quantity;

    private BigDecimal price;

    private String timeInForce;

    private String status;

    private String clientOrderId;

    private Long binanceOrderId;

    private LocalDateTime executedAt;
}
