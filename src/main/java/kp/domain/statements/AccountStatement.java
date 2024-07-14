package kp.domain.statements;

import kp.domain.Account;

/**
 * The account statement.
 *
 * @param account the account
 * @param message the message
 */
public record AccountStatement(Account account, String message) {
}