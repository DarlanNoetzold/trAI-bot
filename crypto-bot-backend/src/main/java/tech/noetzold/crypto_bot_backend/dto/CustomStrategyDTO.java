package tech.noetzold.crypto_bot_backend.dto;

import lombok.Data;
import tech.noetzold.crypto_bot_backend.model.Indicator;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomStrategyDTO {
    private String userId;
    private String name;
    private String symbol;
    private String interval;
    private String position;
    private List<Indicator> indicators;
    private String strategyCode;
}
