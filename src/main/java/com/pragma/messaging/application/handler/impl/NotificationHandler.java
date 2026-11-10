package com.pragma.messaging.application.handler.impl;

import com.pragma.messaging.application.dto.request.SendNotificationRequestDto;
import com.pragma.messaging.application.dto.request.ValidatePinRequestDto;
import com.pragma.messaging.application.dto.response.NotificationResponseDto;
import com.pragma.messaging.application.dto.response.ValidatePinResponseDto;
import com.pragma.messaging.application.handler.INotificationHandler;
import com.pragma.messaging.application.mapper.INotificationResponseMapper;
import com.pragma.messaging.domain.api.INotificationServicePort;
import com.pragma.messaging.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationHandler implements INotificationHandler {

    private final INotificationServicePort notificationServicePort;
    private final INotificationResponseMapper notificationResponseMapper;

    @Override
    public NotificationResponseDto sendOrderReady(SendNotificationRequestDto dto) {
        Notification notification = notificationServicePort.sendOrderReady(
                dto.getOrderId(), dto.getPhone(), dto.getClientName());
        return notificationResponseMapper.toResponse(notification);
    }

    @Override
    public ValidatePinResponseDto validatePin(UUID orderId, ValidatePinRequestDto dto) {
        boolean valid = notificationServicePort.validatePin(orderId, dto.getPin());
        return new ValidatePinResponseDto(valid);
    }
}
