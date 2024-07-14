package kp.domain;

/**
 * The account owner.
 * <p>
 * PESEL - Universal Electronic System for Registration of the Population
 * </p>
 *
 * @param firstName the first name
 * @param lastName  the last name
 * @param pesel     the PESEL
 */
public record Owner(String firstName, String lastName, String pesel) {
}
