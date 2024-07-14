package kp.services;

import kp.domain.Account;
import kp.domain.funds.ExchangedFunds;
import kp.domain.starters.Starter;
import kp.domain.statements.AccountStatement;
import kp.tools.Validators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static kp.Constants.*;

/**
 * The service.
 */
@Slf4j
@Service
public class KpService {
    private final DatabaseService databaseService;
    private final ExchangeRateService exchangeRateService;

    /**
     * The constructor
     *
     * @param databaseService     the database service
     * @param exchangeRateService the exchange rate service
     */
    public KpService(DatabaseService databaseService, ExchangeRateService exchangeRateService) {
        this.databaseService = databaseService;
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Creates the account.
     *
     * @param starter the account starter
     * @return the result
     */
    public boolean createAccount(Starter starter) {

        if (Optional.ofNullable(starter.firstName()).filter(Predicate.not(String::isBlank)).isEmpty() ||
            Optional.ofNullable(starter.lastName()).filter(Predicate.not(String::isBlank)).isEmpty() ||
            Validators.isAmountNotAllowed(starter.amount()) ||
            databaseService.findAccount(starter.pesel()).isPresent() ||
            Validators.ageOfMajorityNotReached(starter.pesel())) {
            log.error("createAccount(): failed on validation, pesel[{}]", starter.pesel());
            return false;
        }
        if (!databaseService.insertAccount(starter)) {
            log.error("createAccount(): account not inserted into database, pesel[{}]", starter.pesel());
            return false;
        }
        log.info("createAccount(): pesel[{}]", starter.pesel());
        return true;
    }

    /**
     * Exchanges the currency.
     *
     * @param exchangedFunds the exchanged funds
     * @return the exchanged funds updated by the exchange transaction
     */
    public ExchangedFunds exchangeCurrency(ExchangedFunds exchangedFunds) {

        final Optional<Account> accountOpt =
                databaseService.findAccount(exchangedFunds.getPesel());
        if (accountOpt.isEmpty()) {
            log.error("exchangeCurrency(): account not found, pesel[{}]",
                    exchangedFunds.getPesel());
            return EXCHANGED_FUNDS_EMPTY_FUN.apply(exchangedFunds.getPesel());
        }
        if (Validators.isAmountNotAllowed(exchangedFunds.getAmountFrom()) ||
            Validators.accountBalanceNotCorrect(accountOpt.get(), exchangedFunds)) {
            log.error("exchangeCurrency(): account balance not valid, pesel[{}]",
                    exchangedFunds.getPesel());
            return EXCHANGED_FUNDS_EMPTY_FUN.apply(exchangedFunds.getPesel());
        }
        final Optional<BigDecimal> exchangeRateOpt = exchangeRateService.getExchangeRate();
        if (exchangeRateOpt.isEmpty()) {
            log.error("exchangeCurrency(): exchange rate not available, pesel[{}]",
                    exchangedFunds.getPesel());
            return EXCHANGED_FUNDS_EMPTY_FUN.apply(exchangedFunds.getPesel());
        }
        compute(exchangedFunds, exchangeRateOpt.get(), accountOpt.get().getSubAccountMap());
        if (!databaseService.updateAccount(accountOpt.get())) {
            log.error("exchangeCurrency(): account not updated in database, pesel[{}]",
                    exchangedFunds.getPesel());
            return EXCHANGED_FUNDS_EMPTY_FUN.apply(exchangedFunds.getPesel());
        }
        exchangedFunds.setMessage(CURRENCY_EXCHANGE_MSG);
        log.info("exchangeCurrency(): pesel[{}]", exchangedFunds.getPesel());
        return exchangedFunds;
    }

    /**
     * Gets the account statement.
     *
     * @param pesel the PESEL
     * @return the account
     */
    public AccountStatement getAccountStatement(String pesel) {

        final Optional<Account> accountOpt = databaseService.findAccount(pesel);
        if (accountOpt.isEmpty()) {
            log.error("getAccountStatement(): account not found in database, pesel[{}]", pesel);
            return ACCOUNT_STATEMENT_EMPTY_FUN.apply(pesel);
        }
        log.info("getAccountStatement(): pesel[{}]", pesel);
        return new AccountStatement(accountOpt.get(), ACCOUNT_STATEMENT_OK_MSG.apply(pesel));
    }

    /**
     * Computes and sets the amounts.
     *
     * @param exchangedFunds the exchanged funds
     * @param exchangeRate   the exchange rate
     * @param subAccountMap  the map with the secondary accounts
     */
    private void compute(ExchangedFunds exchangedFunds, BigDecimal exchangeRate,
                         Map<Currency, BigDecimal> subAccountMap) {

        exchangedFunds.setExchangeRate(exchangeRate);
        final BigDecimal amountFrom = exchangedFunds.getAmountFrom()
                .setScale(6, RoundingMode.HALF_EVEN);
        final BigDecimal amountTo;
        if (exchangedFunds.getCurrencyTo().equals(US_CURRENCY)) {
            // HALF_EVEN is known as bankers' rounding
            amountTo = amountFrom.divide(exchangeRate, RoundingMode.HALF_EVEN);
        } else {
            amountTo = amountFrom.multiply(exchangeRate);
        }
        exchangedFunds.setAmountTo(amountTo);
        subAccountMap.put(exchangedFunds.getCurrencyFrom(),
                subAccountMap.get(exchangedFunds.getCurrencyFrom()).subtract(amountFrom));
        subAccountMap.put(exchangedFunds.getCurrencyTo(),
                subAccountMap.get(exchangedFunds.getCurrencyTo()).add(amountTo));
    }

}
