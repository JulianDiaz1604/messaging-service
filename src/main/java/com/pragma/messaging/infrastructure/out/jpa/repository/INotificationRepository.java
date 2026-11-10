package com.pragma.messaging.infrastructure.out.jpa.repository;

import com.pragma.messaging.infrastructure.out.jpa.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface INotificationRepository extends JpaRepository<NotificationEntity, UUID> {

    Optional<NotificationEntity> findByOrderId(UUID orderId);

    boolean existsByOrderId(UUID orderId);
}
