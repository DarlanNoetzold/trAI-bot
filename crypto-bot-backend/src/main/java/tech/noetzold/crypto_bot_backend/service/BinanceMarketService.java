package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceMarketService {

    private final WebClient binanceWebClient;

    public List getCandles(String symbol, String interval, int limit) {
        return binanceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/klines")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(List.class)
                .block();
    }

    public Map getDepth(String symbol, int limit) {
        return binanceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/depth")
                        .queryParam("symbol", symbol)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map getLastPrice(String symbol) {
        return binanceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/ticker/price")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map getBookTicker(String symbol) {
        return binanceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/ticker/bookTicker")
                        .queryParam("symbol", symbol)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public List<Map> getRecentTrades(String symbol, int limit) {
        return binanceWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/trades")
                        .queryParam("symbol", symbol)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }
}
