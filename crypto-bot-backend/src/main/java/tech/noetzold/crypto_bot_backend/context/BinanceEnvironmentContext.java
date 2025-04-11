package tech.noetzold.crypto_bot_backend.context;

import org.springframework.stereotype.Component;
import tech.noetzold.crypto_bot_backend.enums.BinanceEnvironment;

@Component
public class BinanceEnvironmentContext {

    private static final ThreadLocal<BinanceEnvironment> context = ThreadLocal.withInitial(() -> BinanceEnvironment.TESTNET);

    public static void set(BinanceEnvironment env) {
        context.set(env);
    }

    public static BinanceEnvironment get() {
        return context.get();
    }

    public static void clear() {
        context.remove();
    }
}
