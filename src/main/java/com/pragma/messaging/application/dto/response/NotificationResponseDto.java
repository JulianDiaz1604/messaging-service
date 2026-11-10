package com.pragma.messaging.application.dto.response;

import com.pragma.messaging.domain.model.NotificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NotificationResponseDto {

    private UUID id;
    private UUID orderId;
    private NotificationStatus status;
}
