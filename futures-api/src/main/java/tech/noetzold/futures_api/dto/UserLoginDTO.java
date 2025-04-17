package tech.noetzold.futures_api.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}

