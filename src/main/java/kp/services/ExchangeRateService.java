package kp.services;

import kp.domain.rates.ExchangeRate;
import kp.domain.rates.ExchangeRateTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kp.Constants.NATIONAL_BANK_URL;

/**
 * The exchange rate service form National Bank.
 */
@Slf4j
@Service
public class ExchangeRateService {
    private final RestClient restClient;

    /**
     * The constructor
     *
     * @param restClient the REST client
     */
    public ExchangeRateService(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Gets the exchange rate from National Bank
     *
     * @return the exchange rate
     */
    public Optional<BigDecimal> getExchangeRate() {

        try {
            final ExchangeRateTable exchangeRateTable = restClient.get()
                    .uri(NATIONAL_BANK_URL).retrieve().body(ExchangeRateTable.class);
            return Optional.ofNullable(exchangeRateTable).map(ExchangeRateTable::getRates)
                    .map(List::getFirst).map(ExchangeRate::getMid).map(BigDecimal::new);
        } catch (HttpClientErrorException exception) {
            log.error("getExchangeRate(): exception[{}]", exception.getMessage());
            return Optional.empty();
        }
    }

}
