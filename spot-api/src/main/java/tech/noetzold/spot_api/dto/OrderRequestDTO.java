package tech.noetzold.spot_api.dto;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private String symbol;        // Ex: BTCUSDT
    private String side;          // BUY ou SELL
    private String type;          // MARKET, LIMIT, etc.
    private Double quantity;      // Quantidade da moeda base
    private Double price;         // Preço (para LIMIT ou STOP_LIMIT)
    private String timeInForce;   // Ex: GTC, IOC, etc.
}
