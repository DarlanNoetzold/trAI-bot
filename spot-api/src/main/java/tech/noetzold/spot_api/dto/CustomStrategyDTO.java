package tech.noetzold.spot_api.dto;

import lombok.Data;
import tech.noetzold.spot_api.model.Indicator;

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
