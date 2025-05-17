package tech.noetzold.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public ResponseEntity<String> fallbackAuth() {
        return ResponseEntity.ok("Auth service temporarily unavailable. Please try again later.");
    }

    @GetMapping("/spot")
    public ResponseEntity<String> fallbackSpot() {
        return ResponseEntity.ok("Spot service temporarily unavailable.");
    }

    @GetMapping("/strategy")
    public ResponseEntity<String> fallbackStrategy() {
        return ResponseEntity.ok("Strategy service temporarily unavailable.");
    }

    @GetMapping("/futures")
    public ResponseEntity<String> fallbackFutures() {
        return ResponseEntity.ok("Futures service temporarily unavailable.");
    }
}
