package com.pragma.messaging.infrastructure.exceptionhandler;

import com.pragma.messaging.domain.exception.DomainException;
import com.pragma.messaging.domain.exception.InvalidPinException;
import com.pragma.messaging.domain.exception.NotificationAlreadySentException;
import com.pragma.messaging.domain.exception.NotificationNotFoundException;
import com.pragma.messaging.domain.exception.SmsDeliveryFailedException;
import com.pragma.messaging.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(NotificationAlreadySentException.class)
    public ResponseEntity<Map<String, String>> handleNotificationAlreadySentException(
            NotificationAlreadySentException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotificationNotFoundException(
            NotificationNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(InvalidPinException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPinException(
            InvalidPinException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(SmsDeliveryFailedException.class)
    public ResponseEntity<Map<String, String>> handleSmsDeliveryFailedException(
            SmsDeliveryFailedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Collections.singletonMap(MESSAGE, exception.getMessage()));
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoDataFoundException(
            NoDataFoundException ignoredNoDataFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }
}
