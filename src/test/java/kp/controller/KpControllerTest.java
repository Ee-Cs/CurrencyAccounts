package kp.controller;

import kp.services.KpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static kp.Constants.*;
import static kp.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The tests for the controller.
 */
@SpringBootTest
@AutoConfigureMockMvc
class KpControllerTest {

    private final MockMvc mockMvc;
    @MockBean
    private KpService kpService;
    private static final boolean VERBOSE = false;

    /**
     * The constructor.
     *
     * @param mockMvc the {@link MockMvc}
     */
    KpControllerTest(@Autowired MockMvc mockMvc) {
        super();
        this.mockMvc = mockMvc;
    }

    /**
     * Should create account.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should create account")
    void shouldCreateAccount() throws Exception {
        // GIVEN
        Mockito.when(kpService.createAccount(STARTER)).thenReturn(true);
        final MockHttpServletRequestBuilder requestBuilder = post(CREATE_ACCOUNT_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(STARTER_JSON);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isNoContent());
        resultActions.andExpect(jsonPath("$").doesNotExist());
    }

    /**
     * Should exchange currency.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should exchange currency")
    void shouldExchangeCurrency() throws Exception {
        // GIVEN
        Mockito.when(kpService.exchangeCurrency(EXCHANGED_FUNDS_SUP.get())).thenReturn(EXCHANGED_FUNDS_AFTER_TRANSACTION);
        final MockHttpServletRequestBuilder requestBuilder = post(EXCHANGE_CURRENCY_PATH)
                .contentType(MediaType.APPLICATION_JSON).content(EXCHANGE_CURRENCY_JSON);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("pesel").value(PESEL));
        resultActions.andExpect(jsonPath("amountFrom").value(AMOUNT_FROM));
        resultActions.andExpect(jsonPath("currencyFrom").value(PL_CURRENCY.getCurrencyCode()));
        resultActions.andExpect(jsonPath("amountTo").value(AMOUNT_TO));
        resultActions.andExpect(jsonPath("currencyTo").value(US_CURRENCY.getCurrencyCode()));
        resultActions.andExpect(jsonPath("exchangeRate").value(EXCHANGE_RATE));
        resultActions.andExpect(jsonPath("message").value(CURRENCY_EXCHANGE_MSG));
    }

    /**
     * Should get account statement.
     *
     * @throws Exception the exception
     */
    @Test
    @DisplayName("🟩 should get account statement")
    void shouldGetAccountStatement() throws Exception {
        // GIVEN
        Mockito.when(kpService.getAccountStatement(PESEL)).thenReturn(ACCOUNT_STATEMENT_RECEIVED);
        final MockHttpServletRequestBuilder requestBuilder =
                get(ACCOUNT_STATEMENT_WITH_PESEL_URL).contentType(MediaType.APPLICATION_JSON);
        // WHEN
        final ResultActions resultActions = mockMvc.perform(requestBuilder);
        // THEN
        if (VERBOSE) {
            resultActions.andDo(print());
        }
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("account.owner.firstName").value(FIRST_NAME));
        resultActions.andExpect(jsonPath("account.owner.lastName").value(LAST_NAME));
        resultActions.andExpect(jsonPath("account.owner.pesel").value(PESEL));
        resultActions.andExpect(jsonPath("account.subAccountMap.PLN").value(0));
        resultActions.andExpect(jsonPath("account.subAccountMap.USD").value(0));
    }

}