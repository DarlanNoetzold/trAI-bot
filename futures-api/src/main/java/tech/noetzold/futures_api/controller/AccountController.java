package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.noetzold.futures_api.service.BinanceAccountService;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final BinanceAccountService accountService;

    @GetMapping("/info")
    public ResponseEntity<?> getAccountInfo() {
        return ResponseEntity.ok(accountService.getAccountInfo());
    }

    @GetMapping("/trades")
    public ResponseEntity<?> getAccountTrades(
            @RequestParam String symbol,
            @RequestParam(required = false, defaultValue = "50") Integer limit
    ) {
        return ResponseEntity.ok(accountService.getAccountTrades(symbol, limit));
    }
}
