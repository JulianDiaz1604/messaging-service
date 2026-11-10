package com.pragma.messaging.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SendNotificationRequestDto {

    private UUID orderId;
    private String phone;
    private String clientName;
}
