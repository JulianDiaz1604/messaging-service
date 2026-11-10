package com.pragma.messaging.domain.usecase;

import com.pragma.messaging.domain.exception.InvalidPinException;
import com.pragma.messaging.domain.exception.NotificationAlreadySentException;
import com.pragma.messaging.domain.exception.NotificationNotFoundException;
import com.pragma.messaging.domain.exception.SmsDeliveryFailedException;
import com.pragma.messaging.domain.model.Notification;
import com.pragma.messaging.domain.model.NotificationStatus;
import com.pragma.messaging.domain.spi.INotificationPersistencePort;
import com.pragma.messaging.domain.spi.ISmsSenderPort;
import com.pragma.messaging.domain.validation.NotificationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationUseCaseTest {

    @Mock
    private INotificationPersistencePort notificationPersistencePort;

    @Mock
    private ISmsSenderPort smsSenderPort;

    @Mock
    private NotificationValidator notificationValidator;

    private NotificationUseCase notificationUseCase;

    @BeforeEach
    void setUp() {
        notificationUseCase = new NotificationUseCase(
                notificationPersistencePort, smsSenderPort, notificationValidator);
    }

    // --- sendOrderReady ---

    @Test
    void sendOrderReadyGeneratesPinPersistsAndSendsSms() {
        UUID orderId = UUID.randomUUID();
        String phone = "+573005698325";
        String clientName = "John";
        Notification saved = new Notification(UUID.randomUUID(), orderId, phone, "123456",
                NotificationStatus.SENT, LocalDateTime.now());

        doNothing().when(notificationValidator).validateSendRequest(orderId, phone);
        when(notificationPersistencePort.existsByOrderId(orderId)).thenReturn(false);
        doNothing().when(smsSenderPort).send(anyString(), anyString());
        when(notificationPersistencePort.save(any(Notification.class))).thenReturn(saved);

        Notification result = notificationUseCase.sendOrderReady(orderId, phone, clientName);

        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(orderId);
        assertThat(result.getStatus()).isEqualTo(NotificationStatus.SENT);
        verify(smsSenderPort).send(anyString(), anyString());
        verify(notificationPersistencePort).save(any(Notification.class));
    }

    @Test
    void sendOrderReadyThrowsNotificationAlreadySentExceptionWhenOrderAlreadyExists() {
        UUID orderId = UUID.randomUUID();
        String phone = "+573005698325";

        doNothing().when(notificationValidator).validateSendRequest(orderId, phone);
        when(notificationPersistencePort.existsByOrderId(orderId)).thenReturn(true);

        assertThatThrownBy(() -> notificationUseCase.sendOrderReady(orderId, phone, null))
                .isInstanceOf(NotificationAlreadySentException.class);

        verify(smsSenderPort, never()).send(anyString(), anyString());
        verify(notificationPersistencePort, never()).save(any());
    }

    @Test
    void sendOrderReadyThrowsSmsDeliveryFailedExceptionWhenSnsThrows() {
        UUID orderId = UUID.randomUUID();
        String phone = "+573005698325";

        doNothing().when(notificationValidator).validateSendRequest(orderId, phone);
        when(notificationPersistencePort.existsByOrderId(orderId)).thenReturn(false);
        doThrow(new SmsDeliveryFailedException("SMS failed")).when(smsSenderPort).send(anyString(), anyString());

        assertThatThrownBy(() -> notificationUseCase.sendOrderReady(orderId, phone, null))
                .isInstanceOf(SmsDeliveryFailedException.class);

        verify(notificationPersistencePort, never()).save(any());
    }

    @Test
    void sendOrderReadyIncludesClientNameInSmsWhenProvided() {
        UUID orderId = UUID.randomUUID();
        String phone = "+573005698325";
        String clientName = "Maria";
        Notification saved = new Notification(UUID.randomUUID(), orderId, phone, "000001",
                NotificationStatus.SENT, LocalDateTime.now());

        doNothing().when(notificationValidator).validateSendRequest(orderId, phone);
        when(notificationPersistencePort.existsByOrderId(orderId)).thenReturn(false);
        when(notificationPersistencePort.save(any(Notification.class))).thenReturn(saved);

        notificationUseCase.sendOrderReady(orderId, phone, clientName);

        verify(smsSenderPort).send(anyString(), org.mockito.ArgumentMatchers.contains(clientName));
    }

    // --- validatePin ---

    @Test
    void validatePinReturnsTrueWhenPinMatches() {
        UUID orderId = UUID.randomUUID();
        String pin = "123456";
        Notification notification = new Notification(UUID.randomUUID(), orderId, "+57300", pin,
                NotificationStatus.SENT, LocalDateTime.now());

        doNothing().when(notificationValidator).validatePinRequest(orderId, pin);
        when(notificationPersistencePort.findByOrderId(orderId)).thenReturn(Optional.of(notification));

        boolean result = notificationUseCase.validatePin(orderId, pin);

        assertThat(result).isTrue();
    }

    @Test
    void validatePinThrowsInvalidPinExceptionWhenPinDoesNotMatch() {
        UUID orderId = UUID.randomUUID();
        String correctPin = "123456";
        String wrongPin = "000000";
        Notification notification = new Notification(UUID.randomUUID(), orderId, "+57300", correctPin,
                NotificationStatus.SENT, LocalDateTime.now());

        doNothing().when(notificationValidator).validatePinRequest(orderId, wrongPin);
        when(notificationPersistencePort.findByOrderId(orderId)).thenReturn(Optional.of(notification));

        assertThatThrownBy(() -> notificationUseCase.validatePin(orderId, wrongPin))
                .isInstanceOf(InvalidPinException.class);
    }

    @Test
    void validatePinThrowsNotificationNotFoundExceptionWhenOrderDoesNotExist() {
        UUID orderId = UUID.randomUUID();
        String pin = "123456";

        doNothing().when(notificationValidator).validatePinRequest(orderId, pin);
        when(notificationPersistencePort.findByOrderId(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> notificationUseCase.validatePin(orderId, pin))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}
