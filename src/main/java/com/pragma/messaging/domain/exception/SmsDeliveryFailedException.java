package com.pragma.messaging.domain.exception;

public class SmsDeliveryFailedException extends RuntimeException {

    public SmsDeliveryFailedException(String message) {
        super(message);
    }
}
