package com.pragma.messaging.application.handler;

import com.pragma.messaging.application.dto.request.SendNotificationRequestDto;
import com.pragma.messaging.application.dto.request.ValidatePinRequestDto;
import com.pragma.messaging.application.dto.response.NotificationResponseDto;
import com.pragma.messaging.application.dto.response.ValidatePinResponseDto;

import java.util.UUID;

public interface INotificationHandler {

    NotificationResponseDto sendOrderReady(SendNotificationRequestDto dto);

    ValidatePinResponseDto validatePin(UUID orderId, ValidatePinRequestDto dto);
}
