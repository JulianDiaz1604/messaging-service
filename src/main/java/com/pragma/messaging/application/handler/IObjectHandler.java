package com.pragma.messaging.application.handler;

import com.pragma.messaging.application.dto.request.ObjectRequestDto;
import com.pragma.messaging.application.dto.response.ObjectResponseDto;

import java.util.List;

public interface IObjectHandler {

    void saveObject(ObjectRequestDto objectRequestDto);

    List<ObjectResponseDto> getAllObjects();
}