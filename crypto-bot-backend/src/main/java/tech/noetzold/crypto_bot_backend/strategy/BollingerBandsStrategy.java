package tech.noetzold.crypto_bot_backend.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.crypto_bot_backend.service.BinanceMarketService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("BollingerBandsStrategy")
@RequiredArgsConstructor
public class BollingerBandsStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");
            int period = 20;

            List<List<Object>> candles = marketService.getCandles(symbol, interval, period + 1);
            if (candles.size() <= period) {
                return "⚠️ Não há candles suficientes para Bollinger Bands.";
            }

            List<BigDecimal> closes = candles.stream()
                    .map(c -> new BigDecimal(c.get(4).toString()))
                    .toList();

            BigDecimal ma = calculateMA(closes, period);
            BigDecimal std = calculateSTD(closes, period, ma);
            BigDecimal upper = ma.add(std.multiply(BigDecimal.valueOf(2)));
            BigDecimal lower = ma.subtract(std.multiply(BigDecimal.valueOf(2)));
            BigDecimal lastClose = closes.get(closes.size() - 1);

            String signal = lastClose.compareTo(lower) < 0 ? "COMPRA" :
                    lastClose.compareTo(upper) > 0 ? "VENDA" : "MANTER";

            return String.format("Fechamento: %.2f | BB(%.2f / %.2f) → Sinal: %s", lastClose, lower, upper, signal);
        } catch (Exception e) {
            log.error("❌ Erro ao executar BollingerBandsStrategy: {}", e.getMessage(), e);
            return "Erro ao executar BollingerBandsStrategy: " + e.getMessage();
        }
    }

    private BigDecimal calculateMA(List<BigDecimal> closes, int period) {
        return closes.subList(closes.size() - period, closes.size()).stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(period), 8, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSTD(List<BigDecimal> closes, int period, BigDecimal ma) {
        return BigDecimal.valueOf(
                Math.sqrt(closes.subList(closes.size() - period, closes.size()).stream()
                        .map(c -> c.subtract(ma).pow(2))
                        .mapToDouble(BigDecimal::doubleValue)
                        .sum() / period)
        ).setScale(8, RoundingMode.HALF_UP);
    }
}
