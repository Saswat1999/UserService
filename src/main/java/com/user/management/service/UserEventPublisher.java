package com.user.management.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.user.management.dao.UserEvent;

@Service
public class UserEventPublisher {

    private static final String TOPIC = "user-events";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserEvent(UserEvent userEvent) {
    	
    	userEvent.setTimestamp(LocalDateTime.now());
        kafkaTemplate.send(TOPIC, userEvent.toString());
    }
}
