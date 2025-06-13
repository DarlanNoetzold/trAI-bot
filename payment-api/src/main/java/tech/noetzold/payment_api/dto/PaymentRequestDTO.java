package tech.noetzold.payment_api.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long amount; // Ex: 999 para $9.99
    private String planName;
    private String successUrl;
    private String cancelUrl;
}

