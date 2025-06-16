package tech.noetzold.payment_api.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long amount;
    private String planName;
    private String successUrl;
    private String cancelUrl;
}

