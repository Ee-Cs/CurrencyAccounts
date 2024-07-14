package kp;

import kp.domain.Account;
import kp.domain.Owner;
import kp.domain.funds.ExchangedFunds;
import kp.domain.starters.Starter;
import kp.domain.statements.AccountStatement;

import java.math.BigDecimal;
import java.util.function.Supplier;

import static kp.Constants.*;

/**
 * The test constants.
 */
public class TestConstants {
    public static final String PESEL = "70010171133";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final Starter STARTER = new Starter(
            FIRST_NAME, LAST_NAME, PESEL, BigDecimal.valueOf(MINIMUM_AMOUNT));
    public static final BigDecimal EXCHANGE_RATE = BigDecimal.valueOf(3.21);
    public static final BigDecimal INITIAL_AMOUNT = BigDecimal.valueOf(12.34);
    public static final Supplier<ExchangedFunds> EXCHANGED_FUNDS_SUP = () ->
            new ExchangedFunds(PESEL, INITIAL_AMOUNT, PL_CURRENCY,
                    BigDecimal.ZERO, US_CURRENCY, BigDecimal.ZERO, "");

    public static final BigDecimal AMOUNT_FROM = BigDecimal.valueOf(12.34);
    public static final BigDecimal AMOUNT_TO = BigDecimal.valueOf(3.844237);
    public static final ExchangedFunds EXCHANGED_FUNDS_AFTER_TRANSACTION =
            new ExchangedFunds(PESEL, AMOUNT_FROM, PL_CURRENCY, AMOUNT_TO, US_CURRENCY,
                    EXCHANGE_RATE, CURRENCY_EXCHANGE_MSG);
    public static final BigDecimal AMOUNT_FROM_FOREIGN = BigDecimal.valueOf(3.844237);
    public static final BigDecimal AMOUNT_TO_LOCAL = BigDecimal.valueOf(12.34);
    public static final ExchangedFunds EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION =
            new ExchangedFunds(PESEL, AMOUNT_FROM_FOREIGN, US_CURRENCY, AMOUNT_TO_LOCAL, PL_CURRENCY,
                    EXCHANGE_RATE, CURRENCY_EXCHANGE_MSG);
    public static final Supplier<Account> ACCOUNT_SUP = () ->
            new Account(new Owner(FIRST_NAME, LAST_NAME, PESEL));
    public static final AccountStatement ACCOUNT_STATEMENT_RECEIVED =
            new AccountStatement(ACCOUNT_SUP.get(), ACCOUNT_STATEMENT_OK_MSG.apply(PESEL));
    public static final String ACCOUNT_STATEMENT_WITH_PESEL_URL = ACCOUNT_STATEMENT_PATH + "?pesel=" + PESEL;
    public static final String STARTER_JSON = """
            {
              "pesel"     : "70010171133",
              "firstName" : "John",
              "lastName"  : "Doe",
              "amount"    : 0.05
            }
            """;
    public static final String EXCHANGE_CURRENCY_JSON = """
            {
              "pesel" : "70010171133",
              "amountFrom" : 12.34,
              "currencyFrom" : "PLN",
              "amountTo" : 0,
              "currencyTo" : "USD",
              "exchangeRate" : 0,
              "message" : ""
            }
            """;
}
