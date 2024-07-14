package kp.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import static kp.Constants.PL_CURRENCY;
import static kp.Constants.US_CURRENCY;

/**
 * The account.
 */
@Data
public class Account {
    private Owner owner;
    private final Map<Currency, BigDecimal> subAccountMap = new HashMap<>();

    /**
     * The constructor.
     *
     * @param owner the owner
     */
    public Account(Owner owner) {
        this.owner = owner;
        this.subAccountMap.put(US_CURRENCY, BigDecimal.ZERO);
        this.subAccountMap.put(PL_CURRENCY, BigDecimal.ZERO);
    }
}
