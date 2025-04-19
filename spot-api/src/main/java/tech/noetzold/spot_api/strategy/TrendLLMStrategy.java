package tech.noetzold.spot_api.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.noetzold.spot_api.service.BinanceMarketService;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Component("TrendLLMStrategy")
@RequiredArgsConstructor
public class TrendLLMStrategy implements BaseStrategy {

    private final BinanceMarketService marketService;

    private final RestTemplate restTemplate = new RestTemplate(); // ou injete se preferir

    @Override
    public String run(Map<String, String> params, String apiKey, String secretKey) {
        try {
            String symbol = params.getOrDefault("symbol", "BTCUSDT");
            String interval = params.getOrDefault("interval", "1h");

            List<List<Object>> candles = marketService.getCandles(symbol, interval, 10); // últimos 10 candles
            if (candles.size() < 2) {
                return "⚠️ Não há dados suficientes para análise.";
            }

            StringBuilder promptBuilder = new StringBuilder();
            promptBuilder.append("Analyze the recent price trend for ").append(symbol)
                    .append(" using the closing prices from the last 10 candles below.\n")
                    .append("Based on the trend, decide if the bot should BUY, HOLD, or SELL.\n")
                    .append("Return only one of the words: BUY, HOLD, SELL.\n\n");

            for (int i = 0; i < candles.size(); i++) {
                String close = candles.get(i).get(4).toString(); // índice 4 é o fechamento
                promptBuilder.append("Candle ").append(i + 1).append(": ").append(close).append("\n");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            HttpEntity<String> request = new HttpEntity<>(promptBuilder.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/ollama", request, String.class);

            return String.format("🧠 LLM Decision based on trend: %s", response.getBody().trim());

        } catch (Exception e) {
            log.error("❌ Erro ao executar TrendLLMStrategy: {}", e.getMessage(), e);
            return "Erro ao executar TrendLLMStrategy: " + e.getMessage();
        }
    }
}
