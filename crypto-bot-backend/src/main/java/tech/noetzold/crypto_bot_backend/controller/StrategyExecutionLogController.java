package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.model.StrategyExecutionLog;
import tech.noetzold.crypto_bot_backend.model.User;
import tech.noetzold.crypto_bot_backend.repository.StrategyExecutionLogRepository;
import tech.noetzold.crypto_bot_backend.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
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
        User user = (User) userService.loadUserByUsername(principal.getName());

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<StrategyExecutionLog> logs = (strategyName == null || strategyName.isBlank())
                ? logRepository.findByUserIdOrderByTimestampDesc(user.getId(), pageRequest)
                : logRepository.findByUserIdAndStrategyNameOrderByTimestampDesc(user.getId(), strategyName, pageRequest);

        return ResponseEntity.ok(logs);
    }
}
