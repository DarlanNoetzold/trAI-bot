package tech.noetzold.auth_api.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}

