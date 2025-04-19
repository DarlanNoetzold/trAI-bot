package tech.noetzold.spot_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeDTO {
    private Long id;
    private String symbol;
    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal quoteQty;
    private Long time;
    private Boolean isBuyer;
    private Boolean isMaker;
    private Boolean isBestMatch;
}
