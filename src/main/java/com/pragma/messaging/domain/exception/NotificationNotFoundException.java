package com.pragma.messaging.domain.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String message) {
        super(message);
    }
}
