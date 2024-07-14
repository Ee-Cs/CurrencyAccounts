package kp.services;

import kp.domain.Account;
import kp.domain.funds.ExchangedFunds;
import kp.domain.starters.Starter;
import kp.domain.statements.AccountStatement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static kp.Constants.*;
import static kp.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * The service tests.
 */
@SpringBootTest
class KpServiceTest {

    private final KpService kpService;
    @MockBean
    private DatabaseService databaseService;
    @MockBean
    private ExchangeRateService exchangeRateService;

    /**
     * The constructor.
     *
     * @param kpService the service
     */
    KpServiceTest(@Autowired KpService kpService) {
        this.kpService = kpService;
    }

    /**
     * Executed before each test.
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * Should create the account.
     */
    @Test
    @DisplayName("🟩 should create the account")
    void shouldCreateAccount() {
        // GIVEN
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(STARTER);
        // THEN
        Assertions.assertTrue(result, "account created");
    }

    /**
     * Should exchange the currency - from local to foreign.
     */
    @Test
    @DisplayName("🟩 should exchange the currency to foreign")
    void shouldExchangeCurrencyToForeign() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(EXCHANGED_FUNDS_SUP.get());
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_AFTER_TRANSACTION, actualExchangedFunds,
                "exchanged funds");
    }

    /**
     * Should exchange the currency - from foreign to local.
     */
    @Test
    @DisplayName("🟩 should exchange the currency from foreign")
    void shouldExchangeCurrencyFromForeign() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(US_CURRENCY, AMOUNT_FROM_FOREIGN);
        final ExchangedFunds exchangedFundsForeign = EXCHANGED_FUNDS_SUP.get();
        exchangedFundsForeign.setAmountFrom(AMOUNT_FROM_FOREIGN);
        exchangedFundsForeign.setCurrencyFrom(US_CURRENCY);
        exchangedFundsForeign.setCurrencyTo(PL_CURRENCY);
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(exchangedFundsForeign);
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getPesel(),
                actualExchangedFunds.getPesel(),
                "pesel");
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getAmountFrom(),
                actualExchangedFunds.getAmountFrom(),
                "amount from");
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getCurrencyFrom(),
                actualExchangedFunds.getCurrencyFrom(),
                "currency from");
        // before assert the scale is changed for the actual result rounding
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getAmountTo(),
                actualExchangedFunds.getAmountTo().setScale(2, RoundingMode.HALF_EVEN),
                "amount to");
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getCurrencyTo(),
                actualExchangedFunds.getCurrencyTo(),
                "currency to");
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getExchangeRate(),
                actualExchangedFunds.getExchangeRate(),
                "exchange rate");
        Assertions.assertEquals(EXCHANGED_FUNDS_FOREIGN_AFTER_TRANSACTION.getMessage(),
                actualExchangedFunds.getMessage(),
                ".message");
    }

    /**
     * Should get account statement.
     */
    @Test
    @DisplayName("🟩 should get account statement")
    void shouldGetAccountStatement() {
        // GIVEN
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(ACCOUNT_SUP.get()));
        // WHEN
        final AccountStatement actualAccountStatement = kpService.getAccountStatement(PESEL);
        // THEN
        Assertions.assertEquals(ACCOUNT_STATEMENT_RECEIVED,
                actualAccountStatement, "account statement");
    }

    /**
     * Should not create the account when first name is absent.
     */
    @Test
    @DisplayName("🟥 should not create account - first name is absent")
    void shouldNotCreateAccountFirstNameIsAbsent() {
        // GIVEN
        final Starter starter = new Starter(null, LAST_NAME, PESEL,
                BigDecimal.valueOf(MINIMUM_AMOUNT));
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(starter);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not create the account when last name is absent.
     */
    @Test
    @DisplayName("🟥 should not create account - last name is absent")
    void shouldNotCreateAccountLastNameIsAbsent() {
        // GIVEN
        final Starter starter = new Starter(FIRST_NAME, null, PESEL,
                BigDecimal.valueOf(MINIMUM_AMOUNT));
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(starter);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not create the account when account exists.
     */
    @Test
    @DisplayName("🟥 should not create exiting account")
    void shouldNotCreateExistingAccount() {
        // GIVEN
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(ACCOUNT_SUP.get()));
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(STARTER);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not create the account when the amount is not allowed.
     */
    @Test
    @DisplayName("🟥 should not create account - amount not allowed")
    void shouldNotCreateAccountAmountNotAllowed() {
        // GIVEN
        final Starter starter = new Starter(FIRST_NAME, LAST_NAME, PESEL,
                BigDecimal.valueOf(MINIMUM_AMOUNT - 1));
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(starter);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not create the account when not reached age of majority.
     */
    @Test
    @DisplayName("🟥 should not create account - not reached age of majority")
    void shouldNotCreateAccountNotReachedAgeOfMajority() {
        // GIVEN
        LocalDate pastLocalDate = LocalDate.now().minusYears(18);
        final String peselNotReachedAgeOfMajority = String.format("%02d%d%d10000",
                pastLocalDate.getYear() - 2000, pastLocalDate.getMonthValue() + 20,
                pastLocalDate.getDayOfMonth() + 1);
        final Starter starter = new Starter(FIRST_NAME, LAST_NAME, peselNotReachedAgeOfMajority,
                BigDecimal.valueOf(MINIMUM_AMOUNT));
        Mockito.when(databaseService.findAccount(peselNotReachedAgeOfMajority))
                .thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(true);
        // WHEN
        boolean result = kpService.createAccount(starter);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not create the account when the result was not inserted into the database.
     */
    @Test
    @DisplayName("🟥 should not create account -  not inserted")
    void shouldNotCreateAccountWhenNotInserted() {
        // GIVEN
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        Mockito.when(databaseService.insertAccount(STARTER)).thenReturn(false);
        // WHEN
        boolean result = kpService.createAccount(STARTER);
        // THEN
        Assertions.assertFalse(result, "account not created");
    }

    /**
     * Should not exchange the currency when the account is absent.
     */
    @Test
    @DisplayName("🟥 should not exchange currency - account absent")
    void shouldNotExchangeCurrencyWhenAccountAbsent() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        final ExchangedFunds exchangedFunds =
                new ExchangedFunds(PESEL, INITIAL_AMOUNT.subtract(BigDecimal.ONE), PL_CURRENCY,
                        BigDecimal.ZERO, US_CURRENCY, BigDecimal.ZERO, "");
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(exchangedFunds);
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_EMPTY_FUN.apply(PESEL), actualExchangedFunds,
                "empty exchanged funds");
    }

    /**
     * Should not exchange the currency when the amount is not allowed.
     */
    @Test
    @DisplayName("🟥 should not exchange currency - amount not allowed")
    void shouldNotExchangeCurrencyWhenAmountNotAllowed() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        final ExchangedFunds exchangedFunds =
                new ExchangedFunds(PESEL, BigDecimal.valueOf(MINIMUM_AMOUNT - 1), PL_CURRENCY,
                        BigDecimal.ZERO, US_CURRENCY, BigDecimal.ZERO, "");
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(exchangedFunds);
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_EMPTY_FUN.apply(PESEL), actualExchangedFunds,
                "empty exchanged funds");
    }

    /**
     * Should not exchange the currency when the account balance is not correct.
     */
    @Test
    @DisplayName("🟥 should not exchange currency - account balance not correct")
    void shouldNotExchangeCurrencyWhenAccountBalanceNotCorrect() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT.subtract(BigDecimal.ONE));
        final ExchangedFunds exchangedFunds =
                new ExchangedFunds(PESEL, INITIAL_AMOUNT, PL_CURRENCY,
                        BigDecimal.ZERO, US_CURRENCY, BigDecimal.ZERO, "");
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(exchangedFunds);
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_EMPTY_FUN.apply(PESEL), actualExchangedFunds,
                "empty exchanged funds");
    }

    /**
     * Should not exchange the currency when the exchange rate is absent.
     */
    @Test
    @DisplayName("🟥 should not exchange currency - exchange rate absent")
    void shouldNotExchangeCurrencyWhenExchangeRateAbsent() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.empty());
        Mockito.when(databaseService.updateAccount(any())).thenReturn(true);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(EXCHANGED_FUNDS_SUP.get());
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_EMPTY_FUN.apply(PESEL), actualExchangedFunds,
                "empty exchanged funds");
    }

    /**
     * Should not exchange the currency when not updated in the database.
     */
    @Test
    @DisplayName("🟥 should not exchange currency - not updated")
    void shouldNotExchangeCurrencyWhenNotUpdated() {
        // GIVEN
        final Account account = ACCOUNT_SUP.get();
        account.getSubAccountMap().put(PL_CURRENCY, INITIAL_AMOUNT);
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.of(account));
        Mockito.when(exchangeRateService.getExchangeRate()).thenReturn(Optional.of(EXCHANGE_RATE));
        Mockito.when(databaseService.updateAccount(any())).thenReturn(false);
        // WHEN
        final ExchangedFunds actualExchangedFunds = kpService.exchangeCurrency(EXCHANGED_FUNDS_SUP.get());
        // THEN
        Assertions.assertEquals(EXCHANGED_FUNDS_EMPTY_FUN.apply(PESEL), actualExchangedFunds,
                "empty exchanged funds");
    }

    /**
     * Should not get account statement when the account is absent.
     */
    @Test
    @DisplayName("🟥 should not get account statement - account absent")
    void shouldNotGetAccountStatementWhenAccountAbsent() {
        // GIVEN
        Mockito.when(databaseService.findAccount(PESEL)).thenReturn(Optional.empty());
        // WHEN
        final AccountStatement actualAccountStatement = kpService.getAccountStatement(PESEL);
        // THEN
        Assertions.assertEquals(ACCOUNT_STATEMENT_EMPTY_FUN.apply(PESEL),
                actualAccountStatement, "account statement");
    }

}