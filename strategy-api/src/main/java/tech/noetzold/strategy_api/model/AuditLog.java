package tech.noetzold.strategy_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue
    private Long id;

    private Long userId;
    private String action;
    private String resource;
    private String description;
    private Instant timestamp = Instant.now();
}

