package tech.noetzold.crypto_bot_backend.controller;


import tech.noetzold.crypto_bot_backend.service.BinanceMarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketDataController {

    private final BinanceMarketService marketService;

    @GetMapping("/candles")
    public ResponseEntity<?> getCandles(
            @RequestParam String symbol,
            @RequestParam String interval,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return ResponseEntity.ok(marketService.getCandles(symbol, interval, limit));
    }

    @GetMapping("/depth")
    public ResponseEntity<?> getDepth(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(marketService.getDepth(symbol, limit));
    }

    @GetMapping("/price")
    public ResponseEntity<?> getLastPrice(@RequestParam String symbol) {
        return ResponseEntity.ok(marketService.getLastPrice(symbol));
    }

    @GetMapping("/bookTicker")
    public ResponseEntity<?> getBookTicker(@RequestParam String symbol) {
        return ResponseEntity.ok(marketService.getBookTicker(symbol));
    }

    @GetMapping("/trades")
    public ResponseEntity<?> getRecentTrades(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "50") int limit
    ) {
        return ResponseEntity.ok(marketService.getRecentTrades(symbol, limit));
    }
}