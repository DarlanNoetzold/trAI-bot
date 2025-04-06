package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.model.Strategy;
import tech.noetzold.crypto_bot_backend.service.StrategyService;

import java.util.List;

@RestController
@RequestMapping("/api/strategy")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    @PostMapping
    public ResponseEntity<Strategy> createStrategy(@RequestBody Strategy strategy) {
        return ResponseEntity.ok(strategyService.saveStrategy(strategy));
    }

    @GetMapping
    public ResponseEntity<List<Strategy>> getAllStrategies() {
        return ResponseEntity.ok(strategyService.listAllStrategies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Strategy> getStrategyById(@PathVariable Long id) {
        return strategyService.getStrategyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        strategyService.deleteStrategy(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Strategy> activateStrategy(@PathVariable Long id) {
        return ResponseEntity.ok(strategyService.activateStrategy(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Strategy> deactivateStrategy(@PathVariable Long id) {
        return ResponseEntity.ok(strategyService.deactivateStrategy(id));
    }
}
