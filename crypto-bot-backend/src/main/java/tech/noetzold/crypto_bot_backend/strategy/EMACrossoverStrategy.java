package tech.noetzold.crypto_bot_backend.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.crypto_bot_backend.service.BinanceMarketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("EMACrossoverStrategy")
@RequiredArgsConstructor
public class EMACrossoverStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");
            int limit = 30;

            List<List<Object>> candles = marketService.getCandles(symbol, interval, limit);
            if (candles.size() < limit) {
                return "⚠️ Não há candles suficientes para EMA.";
            }

            List<BigDecimal> closes = candles.stream()
                    .map(c -> new BigDecimal(c.get(4).toString()))
                    .toList();

            BigDecimal ema9 = calculateEMA(closes, 9);
            BigDecimal ema21 = calculateEMA(closes, 21);

            String signal = ema9.compareTo(ema21) > 0 ? "COMPRA" :
                    ema9.compareTo(ema21) < 0 ? "VENDA" : "MANTER";

            return String.format("EMA9: %.2f | EMA21: %.2f → Sinal: %s", ema9, ema21, signal);
        } catch (Exception e) {
            log.error("❌ Erro ao executar EMACrossoverStrategy: {}", e.getMessage(), e);
            return "Erro ao executar EMACrossoverStrategy: " + e.getMessage();
        }
    }

    private BigDecimal calculateEMA(List<BigDecimal> closes, int period) {
        BigDecimal multiplier = BigDecimal.valueOf(2.0 / (period + 1));
        BigDecimal ema = closes.get(0);

        for (int i = 1; i < closes.size(); i++) {
            ema = closes.get(i).subtract(ema).multiply(multiplier).add(ema);
        }

        return ema.setScale(8, BigDecimal.ROUND_HALF_UP);
    }
}
