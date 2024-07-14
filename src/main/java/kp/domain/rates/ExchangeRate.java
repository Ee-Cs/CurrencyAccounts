package kp.domain.rates;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * The exchange rate.
 */
@Data
@AllArgsConstructor
public class ExchangeRate implements Serializable {
    private String no;
    private String effectiveDate;
    private String mid;
}
