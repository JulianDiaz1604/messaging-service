package com.pragma.messaging.application.mapper;

import com.pragma.messaging.application.dto.response.ObjectResponseDto;
import com.pragma.messaging.domain.model.ObjectModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IObjectResponseMapper {
    ObjectResponseDto toResponse(ObjectModel objectModel);

    List<ObjectResponseDto> toResponseList(List<ObjectModel> objectModelList);
}
