package tech.noetzold.notification_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;

    private String testnetApiKey;
    private String testnetSecretKey;
    private String productionApiKey;
    private String productionSecretKey;
    private String whatsappNumber;
    private String telegramChatId;
    private String whatsappApiKey;
}
