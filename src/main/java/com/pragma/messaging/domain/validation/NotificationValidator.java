package com.pragma.messaging.domain.validation;

import com.pragma.messaging.domain.exception.InvalidPhoneFormatException;
import com.pragma.messaging.domain.exception.RequiredFieldException;
import com.pragma.messaging.domain.util.DomainConstants;

import java.util.UUID;

public class NotificationValidator {

    public void validateSendRequest(UUID orderId, String phone) {
        if (orderId == null) {
            throw new RequiredFieldException(DomainConstants.FIELD_REQUIRED_MESSAGE);
        }
        if (phone == null || phone.isBlank()) {
            throw new RequiredFieldException(DomainConstants.FIELD_REQUIRED_MESSAGE);
        }
        if (phone.length() > DomainConstants.PHONE_MAX_LENGTH || !phone.matches(DomainConstants.PHONE_REGEX)) {
            throw new InvalidPhoneFormatException(DomainConstants.INVALID_PHONE_MESSAGE);
        }
    }

    public void validatePinRequest(UUID orderId, String pin) {
        if (orderId == null) {
            throw new RequiredFieldException(DomainConstants.FIELD_REQUIRED_MESSAGE);
        }
        if (pin == null || pin.isBlank()) {
            throw new RequiredFieldException(DomainConstants.FIELD_REQUIRED_MESSAGE);
        }
    }
}
