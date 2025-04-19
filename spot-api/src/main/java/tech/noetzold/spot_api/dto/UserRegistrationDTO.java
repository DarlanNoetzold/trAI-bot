package tech.noetzold.spot_api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegistrationDTO {
    private String username;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String address;
    private String binanceTestnetApiKey;
    private String binanceTestnetSecretKey;
    private String binanceProductionApiKey;
    private String binanceProductionSecretKey;
}
