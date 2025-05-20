package tech.noetzold.scheduler_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class StrategySchedule {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String strategyName;
    private String cronExpression;
    private String jwtToken;
}

