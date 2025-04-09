package tech.noetzold.crypto_bot_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "strategies")
@Data
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(length = 5000)
    private String configurationJson; // pode armazenar os parâmetros da estratégia

    private boolean active;
}
