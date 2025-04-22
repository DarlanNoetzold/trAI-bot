package tech.noetzold.strategy_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    private String type;
    private String userEmail;
    private String userId;
    private String username;
    private String strategyName;
    private String symbol;
    private String environment;
    private String originApi;
    private String action;
    private Map<String, String> parameters;
    private Instant timestamp;
}
