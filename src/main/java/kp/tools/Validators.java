package kp.tools;

import kp.domain.Account;
import kp.domain.funds.ExchangedFunds;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import static kp.Constants.*;

/**
 * The validators.
 */
@Slf4j
public class Validators {

    /**
     * The hidden constructor.
     */
    private Validators() {
        throw new IllegalStateException("Validators class");
    }

    /**
     * Validates an amount is between the allowed minimum and the allowed maximum.
     *
     * @param amount the amount
     * @return the result
     */
    public static boolean isAmountNotAllowed(BigDecimal amount) {
        return Optional.ofNullable(amount).map(BigDecimal::doubleValue)
                .map(val -> val < MINIMUM_AMOUNT || val > MAXIMUM_AMOUNT).orElse(true);
    }

    /**
     * Validates the age of majority.
     *
     * @param pesel the PESEL
     * @return the result
     */
    public static boolean ageOfMajorityNotReached(String pesel) {

        return Optional.ofNullable(pesel)
                .filter(Predicate.not(String::isBlank))
                .filter(psl -> psl.length() == PESEL_LENGTH)
                .map(psl -> {
                    if (Integer.parseInt(psl.substring(2, 4)) < 20) {
                        return LocalDate.of(1900 + Integer.parseInt(psl.substring(0, 2)),
                                Integer.parseInt(psl.substring(2, 4)),
                                Integer.parseInt(psl.substring(4, 6)));
                    } else {
                        return LocalDate.of(2000 + Integer.parseInt(psl.substring(0, 2)),
                                Integer.parseInt(psl.substring(2, 4)) - 20,
                                Integer.parseInt(psl.substring(4, 6)));
                    }
                })
                .map(date -> date.plusYears(18))
                .map(date -> date.isAfter(LocalDate.now())).orElse(false);
    }

    /**
     * Validates the account balance.
     *
     * @param account        the account
     * @param exchangedFunds the exchanged funds
     * @return the result
     */
    public static boolean accountBalanceNotCorrect(
            Account account, ExchangedFunds exchangedFunds) {

        return account.getSubAccountMap().get(exchangedFunds.getCurrencyFrom())
                       .compareTo(exchangedFunds.getAmountFrom()) < 0;
    }

}