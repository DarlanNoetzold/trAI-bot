package tech.noetzold.crypto_bot_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Data
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    private BigDecimal price;

    private BigDecimal quantity;

    private BigDecimal quoteQty;

    private Boolean isBuyer;

    private Boolean isMaker;

    private Boolean isBestMatch;

    private Long binanceTradeId;

    private LocalDateTime executedAt;
}
