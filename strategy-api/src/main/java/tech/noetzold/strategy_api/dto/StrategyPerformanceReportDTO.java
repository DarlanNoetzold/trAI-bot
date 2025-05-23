package tech.noetzold.strategy_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StrategyPerformanceReportDTO {
    private String strategyName;
    private long totalExecutions;
    private long successCount;
    private double successRate;
    private BigDecimal totalProfit;
    private BigDecimal maxDrawdown;
}
