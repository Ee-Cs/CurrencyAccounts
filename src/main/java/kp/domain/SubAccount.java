package kp.domain;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * The secondary account that belongs to a separate parent account.
 *
 * @param amount   the amount
 * @param currency the currency
 */
public record SubAccount(BigDecimal amount, Currency currency) {
}