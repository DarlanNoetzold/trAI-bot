package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.service.FuturesAccountService;

@RestController
@RequestMapping("/api/futures/account")
@RequiredArgsConstructor
public class FuturesAccountController {

    private final FuturesAccountService accountService;

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String asset) {
        return ResponseEntity.ok(accountService.getBalance(asset));
    }

    @GetMapping("/positions")
    public ResponseEntity<?> getOpenPositions() {
        return ResponseEntity.ok(accountService.getOpenPositions());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getFuturesAccountInfo() {
        return ResponseEntity.ok(accountService.getFuturesAccountInfo());
    }
}
