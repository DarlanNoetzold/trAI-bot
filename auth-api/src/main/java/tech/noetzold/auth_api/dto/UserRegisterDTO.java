package tech.noetzold.auth_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String address;
    private String testnetApiKey;
    private String testnetSecretKey;
    private String productionApiKey;
    private String productionSecretKey;
    private String whatsappNumber;
    private String telegramChatId;
    private String whatsappApiKey;
    private String role; // Pode ser "ADMIN", "TRADER" ou "VIEWER"

}

