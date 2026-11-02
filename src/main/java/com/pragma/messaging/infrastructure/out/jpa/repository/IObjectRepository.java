package com.pragma.messaging.infrastructure.out.jpa.repository;

import com.pragma.messaging.infrastructure.out.jpa.entity.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IObjectRepository extends JpaRepository<ObjectEntity, Long> {

}