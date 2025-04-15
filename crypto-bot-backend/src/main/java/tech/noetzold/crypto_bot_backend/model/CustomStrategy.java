package tech.noetzold.crypto_bot_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class CustomStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private String symbol;
    private String interval;
    private Integer limit;

    private BigDecimal buyThreshold;
    private BigDecimal sellThreshold;

    private String indicatorType; // exemplo: RSI, EMA, SMA etc

    @Lob
    private String customLogicCode; // código Python/JavaScript ou pseudo-lógica
}

