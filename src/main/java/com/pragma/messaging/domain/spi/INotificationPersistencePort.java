package com.pragma.messaging.domain.spi;

import com.pragma.messaging.domain.model.Notification;

import java.util.Optional;
import java.util.UUID;

public interface INotificationPersistencePort {

    Notification save(Notification notification);

    Optional<Notification> findByOrderId(UUID orderId);

    boolean existsByOrderId(UUID orderId);
}
