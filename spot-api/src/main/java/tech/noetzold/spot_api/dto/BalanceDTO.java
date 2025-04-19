package tech.noetzold.spot_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private String asset;
    private BigDecimal free;
    private BigDecimal locked;
}
