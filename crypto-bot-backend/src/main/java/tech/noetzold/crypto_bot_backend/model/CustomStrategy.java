package tech.noetzold.crypto_bot_backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Entity
public class CustomStrategy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;
    private String name;
    private String symbol;
    private String interval;
    private String position;

    @ElementCollection
    private List<Indicator> indicators;

    private String strategyCode;

    public String execute(Map<String, String> params, String apiKey, String secretKey) {
        if (indicators == null || indicators.isEmpty()) {
            return "⚠️ Nenhum indicador definido";
        }

        Indicator i = indicators.get(0);
        return String.format("📊 Estratégia '%s' usando %s (%d): entrada se %s %s / saída se %s %s",
                name, i.getType(), i.getPeriod(),
                i.getType(), i.getEntryCondition() + " " + i.getEntryValue(),
                i.getType(), i.getExitCondition() + " " + i.getExitValue());
    }
}
