package kp.services;

import kp.domain.Account;
import kp.domain.Owner;
import kp.domain.SubAccount;
import kp.domain.starters.Starter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static kp.Constants.*;

/**
 * The database service.
 */
@Slf4j
@Service
public class DatabaseService {
    private final JdbcClient jdbcClient;

    /**
     * The constructor
     *
     * @param jdbcClient the JDBC client
     */
    public DatabaseService(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * Inserts the account.
     *
     * @param starter the starter
     * @return the flag
     */
    public boolean insertAccount(Starter starter) {

        int result = jdbcClient.sql(INSERT_OWNER_SQL)
                .param("pesel", starter.pesel())
                .param("firstName", starter.firstName())
                .param("lastName", starter.lastName())
                .update();
        if (result != 1) {
            log.error("insertAccount(): failure on owner, result[{}]", result);
            return false;
        }
        result = jdbcClient.sql(INSERT_SUB_ACCOUNT_SQL)
                .param("pesel", starter.pesel())
                .param("amount", starter.amount().doubleValue())
                .param("currency", PL_CURRENCY.getCurrencyCode())
                .update();
        if (result != 1) {
            log.error("insertAccount(): failure on PL sub_account, result[{}]", result);
            return false;
        }
        result = jdbcClient.sql(INSERT_SUB_ACCOUNT_SQL)
                .param("pesel", starter.pesel())
                .param("amount", 0d)
                .param("currency", US_CURRENCY.getCurrencyCode())
                .update();
        if (result != 1) {
            log.error("insertAccount(): failure on US sub_account, result[{}]", result);
            return false;
        }
        return true;
    }

    /**
     * Finds the account.
     *
     * @param pesel the PESEL
     * @return the account
     */
    public Optional<Account> findAccount(String pesel) {

        final List<Owner> ownerList = jdbcClient.sql(FIND_OWNER_SQL)
                .param("pesel", pesel).query(Owner.class).list();
        if (ownerList.size() != 1) {
            log.info("findAccount(): owner not found, pesel[{}]", pesel);
            return Optional.empty();
        }
        final Account account = new Account(ownerList.getFirst());
        final List<SubAccount> subAccountList = jdbcClient.sql(FIND_SUB_ACCOUNT_SQL)
                .param("pesel", pesel).query(SubAccount.class).list();
        if (subAccountList.size() != 2) {
            log.error("findAccount(): sub accounts not found, pesel[{}]", pesel);
            return Optional.empty();
        }
        subAccountList.forEach(sub -> account.getSubAccountMap()
                .put(sub.currency(), sub.amount())
        );
        return Optional.of(account);
    }

    /**
     * Updates the account.
     *
     * @param account the account
     * @return the result flag
     */
    public boolean updateAccount(Account account) {

        int result = jdbcClient.sql(UPDATE_SUB_ACCOUNT_SQL)
                .param("pesel", account.getOwner().pesel())
                .param("amount", account.getSubAccountMap().get(US_CURRENCY))
                .param("currency", US_CURRENCY.getCurrencyCode())
                .update();
        if (result != 1) {
            log.error("updateAccount(): failure on US sub_account, result[{}]", result);
            return false;
        }
        result = jdbcClient.sql(UPDATE_SUB_ACCOUNT_SQL)
                .param("pesel", account.getOwner().pesel())
                .param("amount", account.getSubAccountMap().get(PL_CURRENCY))
                .param("currency", PL_CURRENCY.getCurrencyCode())
                .update();
        if (result != 1) {
            log.error("updateAccount(): failure on PL sub_account, result[{}]", result);
            return false;
        }
        return true;
    }

}
