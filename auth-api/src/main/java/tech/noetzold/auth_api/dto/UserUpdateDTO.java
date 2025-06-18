package tech.noetzold.auth_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    private String username;
    private String email;
    private LocalDate birthDate;
    private String address;
    private String testnetApiKey;
    private String testnetSecretKey;
    private String productionApiKey;
    private String productionSecretKey;
    private String whatsappNumber;
    private String telegramChatId;
    private String whatsappApiKey;
}