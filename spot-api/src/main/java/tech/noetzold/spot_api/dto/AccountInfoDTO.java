package tech.noetzold.spot_api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountInfoDTO {
    private BigDecimal makerCommission;
    private BigDecimal takerCommission;
    private BigDecimal buyerCommission;
    private BigDecimal sellerCommission;
    private Boolean canTrade;
    private Boolean canWithdraw;
    private Boolean canDeposit;
    private Long updateTime;
    private String accountType;
    private List<BalanceDTO> balances;
}
