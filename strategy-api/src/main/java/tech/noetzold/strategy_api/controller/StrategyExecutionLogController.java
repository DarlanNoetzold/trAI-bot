package tech.noetzold.strategy_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.strategy_api.model.StrategyExecutionLog;
import tech.noetzold.strategy_api.model.User;
import tech.noetzold.strategy_api.repository.StrategyExecutionLogRepository;
import tech.noetzold.strategy_api.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Slf4j
public class StrategyExecutionLogController {

    private final StrategyExecutionLogRepository logRepository;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<StrategyExecutionLog>> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String strategyName,
            Principal principal
    ) {
        log.info("GET /api/logs called by user={} with strategyName={}, page={}, size={}",
                principal.getName(), strategyName, page, size);

        User user = (User) userService.loadUserByUsername(principal.getName());
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<StrategyExecutionLog> logs = (strategyName == null || strategyName.isBlank())
                ? logRepository.findByUserIdOrderByTimestampDesc(user.getId(), pageRequest)
                : logRepository.findByUserIdAndStrategyNameOrderByTimestampDesc(user.getId(), strategyName, pageRequest);

        log.info("Logs returned: count={}, userId={}", logs.getTotalElements(), user.getId());
        return ResponseEntity.ok(logs);
    }
}
