package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.service.FuturesAccountService;

@RestController
@RequestMapping("/api/futures/account")
@RequiredArgsConstructor
@Slf4j
public class FuturesAccountController {

    private final FuturesAccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String asset) {
        log.info("GET /api/futures/account/balance called with asset={}", asset);
        return ResponseEntity.ok(accountService.getBalance(asset));
    }

    @GetMapping("/positions")
    public ResponseEntity<?> getOpenPositions() {
        log.info("GET /api/futures/account/positions called");
        return ResponseEntity.ok(accountService.getOpenPositions());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getFuturesAccountInfo() {
        log.info("GET /api/futures/account/info called");
        return ResponseEntity.ok(accountService.getFuturesAccountInfo());
    }
}
