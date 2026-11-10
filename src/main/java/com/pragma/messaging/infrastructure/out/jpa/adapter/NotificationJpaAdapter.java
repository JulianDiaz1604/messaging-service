package com.pragma.messaging.infrastructure.out.jpa.adapter;

import com.pragma.messaging.domain.model.Notification;
import com.pragma.messaging.domain.spi.INotificationPersistencePort;
import com.pragma.messaging.infrastructure.out.jpa.entity.NotificationEntity;
import com.pragma.messaging.infrastructure.out.jpa.mapper.INotificationEntityMapper;
import com.pragma.messaging.infrastructure.out.jpa.repository.INotificationRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class NotificationJpaAdapter implements INotificationPersistencePort {

    private final INotificationRepository notificationRepository;
    private final INotificationEntityMapper notificationEntityMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = notificationEntityMapper.toEntity(notification);
        NotificationEntity saved = notificationRepository.save(entity);
        return notificationEntityMapper.toModel(saved);
    }

    @Override
    public Optional<Notification> findByOrderId(UUID orderId) {
        return notificationRepository.findByOrderId(orderId)
                .map(notificationEntityMapper::toModel);
    }

    @Override
    public boolean existsByOrderId(UUID orderId) {
        return notificationRepository.existsByOrderId(orderId);
    }
}
