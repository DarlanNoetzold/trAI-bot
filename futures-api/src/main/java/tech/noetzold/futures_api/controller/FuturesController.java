package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.service.FuturesMarketService;

@RestController
@RequestMapping("/api/futures")
@RequiredArgsConstructor
public class FuturesController {

    private final FuturesMarketService futuresMarketService;

    @GetMapping("/price")
    public ResponseEntity<?> getPrice(@RequestParam String symbol) {
        return ResponseEntity.ok(futuresMarketService.getPrice(symbol));
    }

    @GetMapping("/candles")
    public ResponseEntity<?> getCandles(@RequestParam String symbol,
                                        @RequestParam String interval,
                                        @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(futuresMarketService.getCandlesticks(symbol, interval, limit));
    }
}

