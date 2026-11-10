package com.pragma.messaging.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private UUID id;
    private UUID orderId;
    private String phone;
    private String pin;
    private NotificationStatus status;
    private LocalDateTime createdAt;
}
