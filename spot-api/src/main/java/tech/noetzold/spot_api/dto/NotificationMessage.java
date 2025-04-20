package tech.noetzold.spot_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    private String type;              // Ex: TRADE_EXECUTED, STRATEGY_STARTED
    private String userEmail;
    private String userId;
    private String username;
    private String strategyName;
    private String symbol;
    private String environment;       // TESTNET ou PRODUCTION
    private String originApi;         // spot-api, strategy-api, etc.
    private String action;            // Ex: "Placed Buy Order", "Started Strategy"
    private Map<String, String> parameters;
    private Instant timestamp;
}

