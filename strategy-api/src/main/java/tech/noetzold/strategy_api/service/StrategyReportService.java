package tech.noetzold.strategy_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.noetzold.strategy_api.dto.StrategyPerformanceReportDTO;
import tech.noetzold.strategy_api.model.StrategyExecutionLog;
import tech.noetzold.strategy_api.repository.StrategyExecutionLogRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StrategyReportService {

    private final StrategyExecutionLogRepository logRepository;
    private final ProfitabilityService profitabilityService;

    public StrategyPerformanceReportDTO generateReport(String strategyName, Long userId) {
        List<StrategyExecutionLog> logs = logRepository
                .findByUserIdAndStrategyNameOrderByTimestampDesc(userId, strategyName, null)
                .stream()
                .filter(log -> log.getMessage() != null)
                .toList();

        long total = logs.size();
        long success = logs.stream()
                .filter(log -> log.getMessage().contains("✅") || log.getMessage().toLowerCase().contains("compra") || log.getMessage().toLowerCase().contains("venda"))
                .count();

        BigDecimal totalProfit = profitabilityService.calculateRealizedProfit(strategyName);
        BigDecimal maxDrawdown = calculateDrawdown(logs);

        StrategyPerformanceReportDTO dto = new StrategyPerformanceReportDTO();
        dto.setStrategyName(strategyName);
        dto.setTotalExecutions(total);
        dto.setSuccessCount(success);
        dto.setSuccessRate(total == 0 ? 0 : (double) success / total * 100);
        dto.setTotalProfit(totalProfit);
        dto.setMaxDrawdown(maxDrawdown);

        return dto;
    }

    private BigDecimal calculateDrawdown(List<StrategyExecutionLog> logs) {
        BigDecimal peak = BigDecimal.ZERO;
        BigDecimal maxDrawdown = BigDecimal.ZERO;
        BigDecimal cumulative = BigDecimal.ZERO;

        for (StrategyExecutionLog log : logs) {
            String msg = log.getMessage().toLowerCase();
            if (msg.contains("lucro") || msg.contains("profit")) {
                try {
                    BigDecimal profit = extractProfitValue(msg);
                    cumulative = cumulative.add(profit);

                    if (cumulative.compareTo(peak) > 0) {
                        peak = cumulative;
                    }

                    BigDecimal drawdown = peak.subtract(cumulative);
                    if (drawdown.compareTo(maxDrawdown) > 0) {
                        maxDrawdown = drawdown;
                    }
                } catch (Exception ignored) {}
            }
        }

        return maxDrawdown;
    }

    private BigDecimal extractProfitValue(String msg) {
        String[] tokens = msg.replaceAll("[^0-9.,-]", " ").split(" ");
        for (String token : tokens) {
            try {
                return new BigDecimal(token.replace(",", "."));
            } catch (Exception ignored) {}
        }
        return BigDecimal.ZERO;
    }
}
