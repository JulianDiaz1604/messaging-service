package com.pragma.messaging.domain.api;

import com.pragma.messaging.domain.model.Notification;

import java.util.UUID;

public interface INotificationServicePort {

    Notification sendOrderReady(UUID orderId, String phone, String clientName);

    boolean validatePin(UUID orderId, String pin);
}
