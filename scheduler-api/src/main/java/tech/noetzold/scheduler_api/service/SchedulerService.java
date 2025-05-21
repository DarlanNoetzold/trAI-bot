package tech.noetzold.scheduler_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.scheduler_api.model.StrategySchedule;

@Component
@Slf4j
public class SchedulerService {

    @Autowired
    private TaskScheduler scheduler;

    @Value("${strategy-api.url}")
    private String strategyApiUrl;

    public void schedule(StrategySchedule schedule) {
        CronTrigger trigger = new CronTrigger(schedule.getCronExpression());
        scheduler.schedule(() -> {
            try {
                WebClient.create(strategyApiUrl)
                        .post()
                        .uri("/api/strategies/execute/" + schedule.getStrategyName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + schedule.getJwtToken())
                        .retrieve()
                        .toBodilessEntity()
                        .block();
            } catch (Exception e) {
                log.error("Error executing strategy: " + e.getMessage());
            }
        }, trigger);
    }
}
