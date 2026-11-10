package com.pragma.messaging.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidatePinResponseDto {

    private final boolean valid;
}
