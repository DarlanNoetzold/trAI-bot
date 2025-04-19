package tech.noetzold.strategy_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategy_execution_logs")
@Data
public class StrategyExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String strategyName;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(length = 3000)
    private String message;

    private LocalDateTime timestamp = LocalDateTime.now();
}
