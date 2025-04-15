package tech.noetzold.crypto_bot_backend.dto;

import java.math.BigDecimal;

public class CustomStrategyDTO {
    private String name;
    private String description;
    private String symbol;
    private String interval;
    private Integer limit;
    private BigDecimal buyThreshold;
    private BigDecimal sellThreshold;
    private String indicatorType;
    private String customLogicCode;
}
