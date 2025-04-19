package tech.noetzold.spot_api.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.spot_api.service.BinanceMarketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("VolumeSpikeStrategy")
@RequiredArgsConstructor
public class VolumeSpikeStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");
            int limit = 20;

            List<List<Object>> candles = marketService.getCandles(symbol, interval, limit);
            if (candles.size() < limit) {
                return "⚠️ Não há candles suficientes para Volume Spike.";
            }

            List<BigDecimal> volumes = candles.stream()
                    .map(c -> new BigDecimal(c.get(5).toString()))
                    .toList();

            BigDecimal avgVolume = volumes.subList(0, limit - 1).stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(limit - 1), 8, BigDecimal.ROUND_HALF_UP);

            BigDecimal lastVolume = volumes.get(volumes.size() - 1);

            String signal = lastVolume.compareTo(avgVolume.multiply(BigDecimal.valueOf(1.5))) > 0 ? "ATENÇÃO: VOLUME ANORMAL" : "NORMAL";

            return String.format("Volume Médio: %.2f | Volume Atual: %.2f → %s", avgVolume, lastVolume, signal);
        } catch (Exception e) {
            log.error("❌ Erro ao executar VolumeSpikeStrategy: {}", e.getMessage(), e);
            return "Erro ao executar VolumeSpikeStrategy: " + e.getMessage();
        }
    }
}
