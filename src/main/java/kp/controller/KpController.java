package kp.controller;

import kp.domain.funds.ExchangedFunds;
import kp.domain.starters.Starter;
import kp.domain.statements.AccountStatement;
import kp.services.KpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static kp.Constants.*;

/**
 * The controller.
 */
@Slf4j
@RestController
public class KpController {

    private final KpService kpService;

    /**
     * The constructor.
     *
     * @param kpService the {@link KpService}
     */
    public KpController(KpService kpService) {
        this.kpService = kpService;
    }

    /**
     * Creates the account.
     *
     * @param starter the account starter
     * @return the result
     */
    @PostMapping(value = CREATE_ACCOUNT_PATH)
    public ResponseEntity<String> createAccount(@RequestBody Starter starter) {

        if (!kpService.createAccount(starter)) {
            log.error("createAccount(): account not created, pesel[{}]", starter.pesel());
            return ResponseEntity.badRequest().build();
        }
        log.info("createAccount(): pesel[{}]", starter.pesel());
        return ResponseEntity.noContent().build();
    }

    /**
     * Exchanges the currency.
     *
     * @param exchangedFunds the exchanged funds
     * @return the exchanged funds updated by the transaction
     */
    @PostMapping(value = EXCHANGE_CURRENCY_PATH)
    public ExchangedFunds exchangeCurrency(@RequestBody ExchangedFunds exchangedFunds) {

        final ExchangedFunds exchangedFundsResult = kpService.exchangeCurrency(exchangedFunds);
        log.info("exchangeCurrency(): pesel[{}]", exchangedFunds.getPesel());
        return exchangedFundsResult;
    }

    /**
     * Gets the account statement.
     *
     * @param pesel the PESEL
     * @return the account
     */
    @GetMapping(value = ACCOUNT_STATEMENT_PATH)
    public AccountStatement getAccountStatement(String pesel) {

        final AccountStatement accountStatement = kpService.getAccountStatement(pesel);
        log.info("getAccountStatement(): pesel[{}]", pesel);
        return accountStatement;
    }
}
