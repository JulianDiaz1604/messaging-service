package com.pragma.messaging.infrastructure.configuration;

import com.pragma.messaging.domain.api.INotificationServicePort;
import com.pragma.messaging.domain.spi.INotificationPersistencePort;
import com.pragma.messaging.domain.spi.ISmsSenderPort;
import com.pragma.messaging.domain.usecase.NotificationUseCase;
import com.pragma.messaging.domain.validation.NotificationValidator;
import com.pragma.messaging.infrastructure.out.jpa.adapter.NotificationJpaAdapter;
import com.pragma.messaging.infrastructure.out.jpa.mapper.INotificationEntityMapper;
import com.pragma.messaging.infrastructure.out.jpa.repository.INotificationRepository;
import com.pragma.messaging.infrastructure.out.sns.SnsSmsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final INotificationRepository notificationRepository;
    private final INotificationEntityMapper notificationEntityMapper;

    @Bean
    public SnsClient snsClient(@Value("${aws.sns.region:us-east-1}") String region) {
        // Real AWS SNS only. Credentials are resolved from the standard AWS provider chain
        // (AWS_ACCESS_KEY_ID / AWS_SECRET_ACCESS_KEY env vars, shared config file, or an IAM role) —
        // nothing is hardcoded and there is no local endpoint override.
        return SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public NotificationValidator notificationValidator() {
        return new NotificationValidator();
    }

    @Bean
    public INotificationPersistencePort notificationPersistencePort() {
        return new NotificationJpaAdapter(notificationRepository, notificationEntityMapper);
    }

    @Bean
    public ISmsSenderPort smsSenderPort(SnsClient snsClient) {
        return new SnsSmsAdapter(snsClient);
    }

    @Bean
    public INotificationServicePort notificationServicePort(ISmsSenderPort smsSenderPort) {
        return new NotificationUseCase(notificationPersistencePort(), smsSenderPort, notificationValidator());
    }
}
