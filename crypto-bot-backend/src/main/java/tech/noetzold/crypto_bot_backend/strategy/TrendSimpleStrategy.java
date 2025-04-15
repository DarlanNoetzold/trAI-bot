package tech.noetzold.crypto_bot_backend.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.crypto_bot_backend.service.BinanceMarketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("TrendSimpleStrategy")
@RequiredArgsConstructor
public class TrendSimpleStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");

            List<List<Object>> candles = marketService.getCandles(symbol, interval, 2);
            if (candles.size() < 2) {
                return "⚠️ Não há dados suficientes.";
            }

            BigDecimal lastClose = new BigDecimal(candles.get(1).get(4).toString());
            BigDecimal prevClose = new BigDecimal(candles.get(0).get(4).toString());

            String signal = lastClose.compareTo(prevClose) > 0 ? "TENDÊNCIA DE ALTA" :
                    lastClose.compareTo(prevClose) < 0 ? "TENDÊNCIA DE BAIXA" : "ESTÁVEL";

            return String.format("Fechamento Anterior: %.2f | Atual: %.2f → %s", prevClose, lastClose, signal);
        } catch (Exception e) {
            log.error("❌ Erro ao executar TrendSimpleStrategy: {}", e.getMessage(), e);
            return "Erro ao executar TrendSimpleStrategy: " + e.getMessage();
        }
    }
}
