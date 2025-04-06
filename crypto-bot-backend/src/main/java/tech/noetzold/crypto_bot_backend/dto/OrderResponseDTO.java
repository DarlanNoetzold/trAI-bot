package tech.noetzold.crypto_bot_backend.dto;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private String symbol;
    private String side;
    private String type;
    private Double executedQty;
    private String clientOrderId;
}
