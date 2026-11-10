package com.pragma.messaging.domain.spi;

public interface ISmsSenderPort {

    void send(String phone, String message);
}
