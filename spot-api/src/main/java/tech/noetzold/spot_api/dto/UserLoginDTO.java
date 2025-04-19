package tech.noetzold.spot_api.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}

