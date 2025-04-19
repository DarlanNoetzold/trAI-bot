package tech.noetzold.strategy_api.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.strategy_api.service.BinanceMarketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("MACrossoverStrategy")
@RequiredArgsConstructor
public class MACrossoverStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");
            int limit = Integer.parseInt(params.getOrDefault("limit", "100"));

            List<List<Object>> candles = marketService.getCandles(symbol, interval, limit);
            if (candles.size() < 50) {
                return "⚠️ Não há candles suficientes para cálculo de médias móveis.";
            }

            List<BigDecimal> closes = candles.stream()
                    .map(c -> new BigDecimal(c.get(4).toString()))
                    .toList();

            BigDecimal shortMA = calculateMovingAverage(closes, 9);  // Média curta (ex: 9 períodos)
            BigDecimal longMA = calculateMovingAverage(closes, 21);  // Média longa (ex: 21 períodos)

            String signal = shortMA.compareTo(longMA) > 0 ? "COMPRA" :
                    shortMA.compareTo(longMA) < 0 ? "VENDA" : "MANTER";

            return String.format("MA9: %.2f | MA21: %.2f → Sinal: %s", shortMA, longMA, signal);

        } catch (Exception e) {
            log.error("❌ Erro ao executar MACrossoverStrategy: {}", e.getMessage(), e);
            return "Erro ao executar MACrossoverStrategy: " + e.getMessage();
        }
    }

    private BigDecimal calculateMovingAverage(List<BigDecimal> closes, int period) {
        if (closes.size() < period) return BigDecimal.ZERO;
        return closes.subList(closes.size() - period, closes.size()).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);
    }
}
