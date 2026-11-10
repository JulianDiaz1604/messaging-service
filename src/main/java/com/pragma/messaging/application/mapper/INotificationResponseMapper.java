package com.pragma.messaging.application.mapper;

import com.pragma.messaging.application.dto.response.NotificationResponseDto;
import com.pragma.messaging.domain.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface INotificationResponseMapper {

    NotificationResponseDto toResponse(Notification notification);
}
