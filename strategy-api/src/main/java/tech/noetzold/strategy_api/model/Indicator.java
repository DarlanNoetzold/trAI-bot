package tech.noetzold.strategy_api.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class Indicator implements Serializable {
    private String type;
    private int period;
    private String entryCondition;
    private String entryValue;
    private String exitCondition;
    private String exitValue;
}
