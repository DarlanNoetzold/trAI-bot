package tech.noetzold.strategy_api.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.strategy_api.service.BinanceMarketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("RSICrossoverStrategy")
@RequiredArgsConstructor
public class RSICrossoverStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");
            int limit = Integer.parseInt(params.getOrDefault("limit", "100"));

            List<List<Object>> candles = marketService.getCandles(symbol, interval, limit);
            if (candles.size() < 15) {
                return "⚠️ Não há candles suficientes para cálculo de RSI";
            }

            List<BigDecimal> closes = candles.stream()
                    .map(candle -> new BigDecimal(candle.get(4).toString())) // índice 4 = close
                    .toList();

            BigDecimal rsi = calculateRSI(closes);
            String signal = rsi.compareTo(BigDecimal.valueOf(30)) < 0 ? "COMPRA" :
                    rsi.compareTo(BigDecimal.valueOf(70)) > 0 ? "VENDA" : "MANTER";

            return String.format("RSI: %.2f → Sinal: %s", rsi, signal);
        } catch (Exception e) {
            log.error("❌ Erro ao executar RSICrossoverStrategy: {}", e.getMessage(), e);
            return "Erro ao executar RSI: " + e.getMessage();
        }
    }

    private BigDecimal calculateRSI(List<BigDecimal> closes) {
        int period = 14;
        BigDecimal gainSum = BigDecimal.ZERO;
        BigDecimal lossSum = BigDecimal.ZERO;

        for (int i = 1; i <= period; i++) {
            BigDecimal diff = closes.get(i).subtract(closes.get(i - 1));
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                gainSum = gainSum.add(diff);
            } else {
                lossSum = lossSum.add(diff.abs());
            }
        }

        BigDecimal avgGain = gainSum.divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal avgLoss = lossSum.divide(BigDecimal.valueOf(period), 8, BigDecimal.ROUND_HALF_UP);

        if (avgLoss.equals(BigDecimal.ZERO)) {
            return BigDecimal.valueOf(100);
        }

        BigDecimal rs = avgGain.divide(avgLoss, 8, BigDecimal.ROUND_HALF_UP);
        return BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(100).divide(BigDecimal.ONE.add(rs), 2, BigDecimal.ROUND_HALF_UP));
    }
}
