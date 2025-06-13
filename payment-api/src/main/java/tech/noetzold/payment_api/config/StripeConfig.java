package tech.noetzold.payment_api.config;


import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
}
