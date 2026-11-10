package com.pragma.messaging.infrastructure.out.jpa.mapper;

import com.pragma.messaging.domain.model.Notification;
import com.pragma.messaging.infrastructure.out.jpa.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface INotificationEntityMapper {

    NotificationEntity toEntity(Notification notification);

    Notification toModel(NotificationEntity entity);
}
