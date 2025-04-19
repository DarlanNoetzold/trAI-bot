package tech.noetzold.strategy_api.controller;

import tech.noetzold.strategy_api.service.BinanceMarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@Slf4j
public class MarketDataController {

    private final BinanceMarketService marketService;

    @GetMapping("/candles")
    public ResponseEntity<?> getCandles(
            @RequestParam String symbol,
            @RequestParam String interval,
            @RequestParam(defaultValue = "50") int limit
    ) {
        log.info("GET /api/market/candles called with symbol={}, interval={}, limit={}", symbol, interval, limit);
        return ResponseEntity.ok(marketService.getCandles(symbol, interval, limit));
    }

    @GetMapping("/depth")
    public ResponseEntity<?> getDepth(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("GET /api/market/depth called with symbol={}, limit={}", symbol, limit);
        return ResponseEntity.ok(marketService.getDepth(symbol, limit));
    }

    @GetMapping("/price")
    public ResponseEntity<?> getLastPrice(@RequestParam String symbol) {
        log.info("GET /api/market/price called with symbol={}", symbol);
        return ResponseEntity.ok(marketService.getLastPrice(symbol));
    }

    @GetMapping("/bookTicker")
    public ResponseEntity<?> getBookTicker(@RequestParam String symbol) {
        log.info("GET /api/market/bookTicker called with symbol={}", symbol);
        return ResponseEntity.ok(marketService.getBookTicker(symbol));
    }

    @GetMapping("/trades")
    public ResponseEntity<?> getRecentTrades(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "50") int limit
    ) {
        log.info("GET /api/market/trades called with symbol={}, limit={}", symbol, limit);
        return ResponseEntity.ok(marketService.getRecentTrades(symbol, limit));
    }
}
