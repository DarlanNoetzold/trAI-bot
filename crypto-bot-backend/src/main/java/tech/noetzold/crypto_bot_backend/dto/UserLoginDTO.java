package tech.noetzold.crypto_bot_backend.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}

