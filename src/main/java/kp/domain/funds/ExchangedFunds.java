package kp.domain.funds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * The funds to exchange.
 * After the completed exchange it is returned
 * as a currency exchange receipt containing the used exchange rate.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangedFunds {
    private String pesel;
    private BigDecimal amountFrom;
    private Currency currencyFrom;
    private BigDecimal amountTo;
    private Currency currencyTo;
    private BigDecimal exchangeRate;
    private String message;
}