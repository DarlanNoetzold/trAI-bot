package tech.noetzold.scheduler_api.model;


import lombok.Data;

@Data
public class StrategySchedule {

    private Long id;
    private Long userId;
    private String strategyName;
    private String cronExpression;
    private String jwtToken;
}

