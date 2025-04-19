package tech.noetzold.spot_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "balances")
@Data
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asset;

    private BigDecimal free;

    private BigDecimal locked;

    private Long updateTime;
}
