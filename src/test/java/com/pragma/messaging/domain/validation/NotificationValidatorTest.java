package com.pragma.messaging.domain.validation;

import com.pragma.messaging.domain.exception.InvalidPhoneFormatException;
import com.pragma.messaging.domain.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationValidatorTest {

    private static final UUID ORDER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private NotificationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new NotificationValidator();
    }

    // --- validateSendRequest ---

    @Test
    void validateSendRequestThrowsRequiredFieldExceptionWhenOrderIdIsNull() {
        assertThatThrownBy(() -> validator.validateSendRequest(null, "+573005698325"))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validateSendRequestThrowsRequiredFieldExceptionWhenPhoneIsNull() {
        assertThatThrownBy(() -> validator.validateSendRequest(ORDER_ID, null))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validateSendRequestThrowsRequiredFieldExceptionWhenPhoneIsBlank() {
        assertThatThrownBy(() -> validator.validateSendRequest(ORDER_ID, "   "))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validateSendRequestThrowsInvalidPhoneFormatExceptionWhenPhoneExceedsMaxLength() {
        assertThatThrownBy(() -> validator.validateSendRequest(ORDER_ID, "+57300569832500"))
                .isInstanceOf(InvalidPhoneFormatException.class);
    }

    @Test
    void validateSendRequestThrowsInvalidPhoneFormatExceptionWhenPhoneContainsLetters() {
        assertThatThrownBy(() -> validator.validateSendRequest(ORDER_ID, "phone123"))
                .isInstanceOf(InvalidPhoneFormatException.class);
    }

    @Test
    void validateSendRequestThrowsInvalidPhoneFormatExceptionWhenPhoneContainsSpecialChars() {
        assertThatThrownBy(() -> validator.validateSendRequest(ORDER_ID, "57-300-5698325"))
                .isInstanceOf(InvalidPhoneFormatException.class);
    }

    @Test
    void validateSendRequestDoesNotThrowForValidPhoneWithPlusPrefix() {
        assertThatCode(() -> validator.validateSendRequest(ORDER_ID, "+573005698325"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateSendRequestDoesNotThrowForValidPhoneWithoutPlusPrefix() {
        assertThatCode(() -> validator.validateSendRequest(ORDER_ID, "3005698325"))
                .doesNotThrowAnyException();
    }

    // --- validatePinRequest ---

    @Test
    void validatePinRequestThrowsRequiredFieldExceptionWhenOrderIdIsNull() {
        assertThatThrownBy(() -> validator.validatePinRequest(null, "123456"))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validatePinRequestThrowsRequiredFieldExceptionWhenPinIsNull() {
        assertThatThrownBy(() -> validator.validatePinRequest(ORDER_ID, null))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validatePinRequestThrowsRequiredFieldExceptionWhenPinIsBlank() {
        assertThatThrownBy(() -> validator.validatePinRequest(ORDER_ID, "   "))
                .isInstanceOf(RequiredFieldException.class);
    }

    @Test
    void validatePinRequestDoesNotThrowForValidRequest() {
        assertThatCode(() -> validator.validatePinRequest(ORDER_ID, "123456"))
                .doesNotThrowAnyException();
    }
}
