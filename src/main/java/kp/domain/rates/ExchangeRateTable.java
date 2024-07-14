package kp.domain.rates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The exchange rate table.
 */
@Data
@AllArgsConstructor
public class ExchangeRateTable implements Serializable {
    private String table;
    private String currency;
    private String code;
    private List<ExchangeRate> rates;
}
