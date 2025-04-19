package tech.noetzold.strategy_api.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}

