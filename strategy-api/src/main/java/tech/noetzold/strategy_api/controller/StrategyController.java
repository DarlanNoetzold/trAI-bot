package tech.noetzold.strategy_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.strategy_api.model.User;
import tech.noetzold.strategy_api.repository.UserRepository;
import tech.noetzold.strategy_api.service.StrategyRunnerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
@Slf4j
public class StrategyController {

    private final StrategyRunnerService strategyRunnerService;
    private final UserRepository userRepository;

    private User extractUserFromHeaders(Map<String, String> headers) {
        String email = headers.get("x-email");
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Missing X-EMAIL header");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listStrategies() {
        log.info("GET /api/strategies/list called");
        return ResponseEntity.ok(strategyRunnerService.listAvailableStrategies());
    }

    @PostMapping("/run/{strategyName}")
    public ResponseEntity<String> runStrategy(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params,
            @RequestHeader Map<String, String> headers
    ) {
        User user = extractUserFromHeaders(headers);
        log.info("POST /api/strategies/run/{} called by user={} with params={}", strategyName, user.getEmail(), params);
        String result = strategyRunnerService.runStrategyAsync(strategyName, params, user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/run-once/{strategyName}")
    public ResponseEntity<String> runStrategyOnce(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params,
            @RequestHeader Map<String, String> headers
    ) {
        User user = extractUserFromHeaders(headers);
        log.info("POST /api/strategies/run-once/{} called by user={} with params={}", strategyName, user.getEmail(), params);
        String result = strategyRunnerService.runStrategy(strategyName, params, user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/stop/{strategyName}")
    public ResponseEntity<String> stopStrategy(@PathVariable String strategyName) {
        log.info("POST /api/strategies/stop/{} called", strategyName);
        String result = strategyRunnerService.stopStrategy(strategyName);
        return ResponseEntity.ok(result);
    }
}
