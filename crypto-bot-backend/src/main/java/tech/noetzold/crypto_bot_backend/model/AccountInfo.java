package tech.noetzold.crypto_bot_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account_info")
@Data
public class AccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal makerCommission;

    private BigDecimal takerCommission;

    private BigDecimal buyerCommission;

    private BigDecimal sellerCommission;

    private Boolean canTrade;

    private Boolean canWithdraw;

    private Boolean canDeposit;

    private Long updateTime;

    private String accountType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_info_id")
    private List<Balance> balances;
}
