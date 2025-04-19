package tech.noetzold.strategy_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.strategy_api.model.StrategyExecutionLog;
import tech.noetzold.strategy_api.repository.StrategyExecutionLogRepository;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Slf4j
public class StrategyExecutionLogController {

    private final StrategyExecutionLogRepository logRepository;

    @GetMapping
    public ResponseEntity<Page<StrategyExecutionLog>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String strategyName,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestHeader("X-EMAIL") String email
    ) {
        log.info("GET /api/logs called by user={} (ID: {}) with strategyName={}, page={}, size={}",
                email, userId, strategyName, page, size);

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<StrategyExecutionLog> logs = (strategyName == null || strategyName.isBlank())
                ? logRepository.findByUserIdOrderByTimestampDesc(userId, pageRequest)
                : logRepository.findByUserIdAndStrategyNameOrderByTimestampDesc(userId, strategyName, pageRequest);

        log.info("Logs returned: count={}, userId={}", logs.getTotalElements(), userId);
        return ResponseEntity.ok(logs);
    }
}
