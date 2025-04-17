package tech.noetzold.futures_api.dto;

import lombok.Data;

@Data
public class FuturesOrderDTO {
    private String symbol;
    private String side; // BUY or SELL
    private String type; // MARKET, LIMIT etc.
    private Double quantity;
    private Double price; // opcional para market
    private String timeInForce; // GTC, IOC...
}

