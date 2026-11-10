package com.pragma.messaging.domain.exception;

public class RequiredFieldException extends DomainException {

    public RequiredFieldException(String message) {
        super(message);
    }
}
