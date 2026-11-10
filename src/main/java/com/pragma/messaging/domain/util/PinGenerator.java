package com.pragma.messaging.domain.util;

import java.security.SecureRandom;

public final class PinGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PinGenerator() {
    }

    /**
     * Generates a random 6-digit numeric PIN, zero-padded (e.g. "004321").
     */
    public static String generate() {
        return format(SECURE_RANDOM.nextInt(DomainConstants.PIN_BOUND));
    }

    /**
     * Formats an integer as a zero-padded PIN of {@link DomainConstants#PIN_LENGTH} digits.
     * Package-private so unit tests can verify the formatting in isolation.
     */
    static String format(int value) {
        return String.format("%0" + DomainConstants.PIN_LENGTH + "d", value);
    }
}
