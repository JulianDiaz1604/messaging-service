package com.pragma.messaging.domain.util;

public final class DomainConstants {

    private DomainConstants() {
    }

    // --- Phone validation ---
    public static final int PHONE_MAX_LENGTH = 13;
    public static final String PHONE_REGEX = "^\\+?[0-9]+$";

    // --- PIN generation ---
    public static final int PIN_LENGTH = 6;
    public static final int PIN_BOUND = 1_000_000;

    // --- Validation error messages ---
    public static final String FIELD_REQUIRED_MESSAGE = "All fields are required";
    public static final String INVALID_PHONE_MESSAGE =
            "The phone must be numeric, may start with '+', and have at most 13 characters";

    // --- Business error messages ---
    public static final String NOTIFICATION_ALREADY_SENT_MESSAGE =
            "A notification has already been sent for this order";
    public static final String NOTIFICATION_NOT_FOUND_MESSAGE =
            "No notification found for the given order";
    public static final String INVALID_PIN_MESSAGE =
            "The provided PIN does not match the one sent to the client";
    public static final String SMS_DELIVERY_FAILED_MESSAGE =
            "The SMS could not be delivered; please try again later";

    // --- SMS message templates ---
    public static final String SMS_TEMPLATE_WITH_NAME =
            "Hello %s! Your order is ready. Present this PIN to collect: %s";
    public static final String SMS_TEMPLATE_WITHOUT_NAME =
            "Your order is ready! Present this PIN to collect: %s";
}
