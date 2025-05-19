package tech.noetzold.scheduler_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.noetzold.scheduler_api.model.StrategySchedule;
import tech.noetzold.scheduler_api.service.SchedulerService;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @PostMapping
    public ResponseEntity<String> scheduleStrategy(@RequestBody StrategySchedule schedule) {
        schedulerService.schedule(schedule);
        return ResponseEntity.ok("Scheduled");
    }
}

