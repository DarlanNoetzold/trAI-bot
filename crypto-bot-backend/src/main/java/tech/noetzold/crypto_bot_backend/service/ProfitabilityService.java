package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfitabilityService {

    private final BinanceAccountService accountService;

    public BigDecimal calculateRealizedProfit(String symbol) {
        List<Map> trades = accountService.getAccountTrades(symbol, 1000);

        BigDecimal totalBuy = BigDecimal.ZERO;
        BigDecimal totalSell = BigDecimal.ZERO;

        for (Map<String, Object> trade : trades) {
            BigDecimal price = new BigDecimal(trade.get("price").toString());
            BigDecimal qty = new BigDecimal(trade.get("qty").toString());
            BigDecimal total = price.multiply(qty);
            boolean isBuyer = Boolean.parseBoolean(trade.get("isBuyer").toString());

            if (isBuyer) {
                totalBuy = totalBuy.add(total);
            } else {
                totalSell = totalSell.add(total);
            }
        }

        return totalSell.subtract(totalBuy);
    }
}
