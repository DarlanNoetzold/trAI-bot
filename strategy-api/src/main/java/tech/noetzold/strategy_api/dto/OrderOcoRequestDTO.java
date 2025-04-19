package tech.noetzold.strategy_api.dto;

import lombok.Data;

@Data
public class OrderOcoRequestDTO {
    private String symbol;
    private String side;           // BUY ou SELL
    private Double quantity;
    private Double price;          // Preço da ordem limit
    private Double stopPrice;      // Preço de ativação da stop
    private Double stopLimitPrice; // Preço real da stop-limit
    private String timeInForce;    // Ex: GTC
}
