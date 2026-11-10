package com.pragma.messaging.domain.usecase;

import com.pragma.messaging.domain.api.INotificationServicePort;
import com.pragma.messaging.domain.exception.InvalidPinException;
import com.pragma.messaging.domain.exception.NotificationAlreadySentException;
import com.pragma.messaging.domain.exception.NotificationNotFoundException;
import com.pragma.messaging.domain.model.Notification;
import com.pragma.messaging.domain.model.NotificationStatus;
import com.pragma.messaging.domain.spi.INotificationPersistencePort;
import com.pragma.messaging.domain.spi.ISmsSenderPort;
import com.pragma.messaging.domain.util.DomainConstants;
import com.pragma.messaging.domain.util.PinGenerator;
import com.pragma.messaging.domain.validation.NotificationValidator;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationUseCase implements INotificationServicePort {

    private final INotificationPersistencePort notificationPersistencePort;
    private final ISmsSenderPort smsSenderPort;
    private final NotificationValidator notificationValidator;

    public NotificationUseCase(INotificationPersistencePort notificationPersistencePort,
                                ISmsSenderPort smsSenderPort,
                                NotificationValidator notificationValidator) {
        this.notificationPersistencePort = notificationPersistencePort;
        this.smsSenderPort = smsSenderPort;
        this.notificationValidator = notificationValidator;
    }

    @Override
    public Notification sendOrderReady(UUID orderId, String phone, String clientName) {
        notificationValidator.validateSendRequest(orderId, phone);

        if (notificationPersistencePort.existsByOrderId(orderId)) {
            throw new NotificationAlreadySentException(DomainConstants.NOTIFICATION_ALREADY_SENT_MESSAGE);
        }

        String pin = PinGenerator.generate();
        String smsMessage = buildSmsMessage(pin, clientName);

        // The SMS port already translates provider failures into SmsDeliveryFailedException,
        // so no defensive catch is needed here and the order is persisted only after a successful send.
        smsSenderPort.send(phone, smsMessage);

        Notification notification = new Notification(null, orderId, phone, pin, NotificationStatus.SENT, LocalDateTime.now());
        return notificationPersistencePort.save(notification);
    }

    @Override
    public boolean validatePin(UUID orderId, String pin) {
        notificationValidator.validatePinRequest(orderId, pin);

        Notification notification = notificationPersistencePort.findByOrderId(orderId)
                .orElseThrow(() -> new NotificationNotFoundException(DomainConstants.NOTIFICATION_NOT_FOUND_MESSAGE));

        if (!notification.getPin().equals(pin)) {
            throw new InvalidPinException(DomainConstants.INVALID_PIN_MESSAGE);
        }

        return true;
    }

    private String buildSmsMessage(String pin, String clientName) {
        if (clientName != null && !clientName.isBlank()) {
            return String.format(DomainConstants.SMS_TEMPLATE_WITH_NAME, clientName, pin);
        }
        return String.format(DomainConstants.SMS_TEMPLATE_WITHOUT_NAME, pin);
    }
}
