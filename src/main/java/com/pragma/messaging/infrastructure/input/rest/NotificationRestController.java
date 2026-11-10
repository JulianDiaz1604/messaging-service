package com.pragma.messaging.infrastructure.input.rest;

import com.pragma.messaging.application.dto.request.SendNotificationRequestDto;
import com.pragma.messaging.application.dto.request.ValidatePinRequestDto;
import com.pragma.messaging.application.dto.response.NotificationResponseDto;
import com.pragma.messaging.application.dto.response.ValidatePinResponseDto;
import com.pragma.messaging.application.handler.INotificationHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "SMS notification and security PIN management for food-court orders")
public class NotificationRestController {

    private final INotificationHandler notificationHandler;

    @Operation(summary = "Notify client that order is ready",
            description = "Generates a 6-digit security PIN, sends it to the client by SMS via AWS SNS, "
                    + "and persists the notification. The PIN is NOT returned in the response — it travels only by SMS.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notification sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Missing or invalid request fields", content = @Content),
            @ApiResponse(responseCode = "409", description = "A notification was already sent for this order", content = @Content),
            @ApiResponse(responseCode = "502", description = "SMS delivery failed", content = @Content)
    })
    @PostMapping("/order-ready")
    public ResponseEntity<NotificationResponseDto> sendOrderReady(
            @RequestBody SendNotificationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationHandler.sendOrderReady(dto));
    }

    @Operation(summary = "Validate order-ready security PIN",
            description = "Verifies that the PIN provided by the client matches the one sent by SMS for the given order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PIN is valid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidatePinResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Missing required fields", content = @Content),
            @ApiResponse(responseCode = "403", description = "PIN does not match", content = @Content),
            @ApiResponse(responseCode = "404", description = "No notification found for the given order", content = @Content)
    })
    @PostMapping("/{orderId}/validate")
    public ResponseEntity<ValidatePinResponseDto> validatePin(
            @PathVariable UUID orderId,
            @RequestBody ValidatePinRequestDto dto) {
        return ResponseEntity.ok(notificationHandler.validatePin(orderId, dto));
    }
}
