package tech.noetzold.strategy_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.strategy_api.dto.StrategyPerformanceReportDTO;
import tech.noetzold.strategy_api.service.StrategyReportService;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyReportController {

    private final StrategyReportService reportService;

    @GetMapping("/report/{strategyName}")
    public ResponseEntity<StrategyPerformanceReportDTO> getReport(
            @PathVariable String strategyName,
            @RequestHeader("X-USER-ID") Long userId) {
        StrategyPerformanceReportDTO report = reportService.generateReport(strategyName, userId);
        return ResponseEntity.ok(report);
    }
}
