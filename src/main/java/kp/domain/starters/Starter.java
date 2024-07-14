package kp.domain.starters;

import java.math.BigDecimal;

/**
 * The account starter with owner data and initial amount.
 *
 * @param firstName the first name
 * @param lastName  the last name
 * @param pesel     the PESEL
 * @param amount    the amount
 */
public record Starter(String firstName, String lastName, String pesel, BigDecimal amount) {
}
