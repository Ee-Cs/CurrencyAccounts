package kp;

import kp.domain.Account;
import kp.domain.Owner;
import kp.domain.funds.ExchangedFunds;
import kp.domain.statements.AccountStatement;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.function.Function;

/**
 * The constants.
 */
public class Constants {
    public static final int PESEL_LENGTH = 11;
    public static final double MINIMUM_AMOUNT = 0.05;
    public static final double MAXIMUM_AMOUNT = 1_000_000;
    public static final Currency US_CURRENCY = Currency.getInstance(Locale.US);
    public static final Currency PL_CURRENCY = Currency.getInstance(Locale.of("pl", "PL"));
    public static final Function<String, ExchangedFunds> EXCHANGED_FUNDS_EMPTY_FUN = pesel ->
            new ExchangedFunds(pesel, BigDecimal.ZERO, Currency.getInstance("XXX"),
                    BigDecimal.ZERO, Currency.getInstance("XXX"), BigDecimal.ZERO,
                    "Currency exchange error");
    public static final Function<String, AccountStatement> ACCOUNT_STATEMENT_EMPTY_FUN = pesel ->
            new AccountStatement(new Account(new Owner("", "", pesel)), "Account not found");
    public static final Function<String, String> ACCOUNT_STATEMENT_OK_MSG =
            pesel -> String.format("The account statement, PESEL '%s'", pesel);
    public static final String CURRENCY_EXCHANGE_MSG = "The currency exchange receipt";
    public static final String INSERT_OWNER_SQL = """
            INSERT INTO owner(pesel, first_name, last_name)
            VALUES (:pesel, :firstName, :lastName)
            """;
    public static final String INSERT_SUB_ACCOUNT_SQL = """
            INSERT INTO sub_account(pesel, amount, currency)
            VALUES (:pesel, :amount, :currency)
            """;
    public static final String UPDATE_SUB_ACCOUNT_SQL = """
            UPDATE sub_account
            SET amount = :amount
            WHERE pesel = :pesel AND currency = :currency
            """;
    public static final String FIND_OWNER_SQL = """
            SELECT pesel, first_name, last_name
            FROM owner
            WHERE pesel = :pesel
            """;
    public static final String FIND_SUB_ACCOUNT_SQL = """
            SELECT pesel, amount, currency
            FROM sub_account
            WHERE pesel = :pesel
            """;
    public static final String NATIONAL_BANK_URL = "http://api.nbp.pl/api/exchangerates/rates/a/usd/";
    public static final String CREATE_ACCOUNT_PATH = "/create_account";
    public static final String EXCHANGE_CURRENCY_PATH = "/exchange_currency";
    public static final String ACCOUNT_STATEMENT_PATH = "/account_statement";
}