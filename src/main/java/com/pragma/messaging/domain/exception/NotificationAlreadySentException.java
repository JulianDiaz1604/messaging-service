package com.pragma.messaging.domain.exception;

public class NotificationAlreadySentException extends RuntimeException {

    public NotificationAlreadySentException(String message) {
        super(message);
    }
}
