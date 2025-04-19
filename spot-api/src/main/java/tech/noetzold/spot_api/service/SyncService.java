package tech.noetzold.spot_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {

    private final BinanceMarketService marketService;
    private final BinanceAccountService accountService;

    public List<List<Object>> syncCandles(String symbol, String interval, int limit) {
        log.info("📡 [syncCandles] Syncing candles | symbol={}, interval={}, limit={}", symbol, interval, limit);
        return marketService.getCandles(symbol, interval, limit);
    }

    public List<Map<String, Object>> syncAccountTrades(String symbol) {
        log.info("📡 [syncAccountTrades] Syncing account trades | symbol={}", symbol);
        return accountService.getAccountTrades(symbol, 1000);
    }
}
