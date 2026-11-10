package com.pragma.messaging.infrastructure.out.sns;

import com.pragma.messaging.domain.exception.SmsDeliveryFailedException;
import com.pragma.messaging.domain.spi.ISmsSenderPort;
import com.pragma.messaging.domain.util.DomainConstants;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

@RequiredArgsConstructor
public class SnsSmsAdapter implements ISmsSenderPort {

    private final SnsClient snsClient;

    @Override
    public void send(String phone, String message) {
        try {
            snsClient.publish(PublishRequest.builder()
                    .phoneNumber(phone)
                    .message(message)
                    .build());
        } catch (SdkException e) {
            // Any AWS SDK failure (service error, client error, missing credentials) is translated
            // at this boundary into the domain exception, so the domain stays provider-agnostic.
            throw new SmsDeliveryFailedException(DomainConstants.SMS_DELIVERY_FAILED_MESSAGE);
        }
    }
}
