package org.course.gardenappgardenengine.shared.audit.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.shared.audit.AuditLog;
import org.course.gardenappgardenengine.shared.audit.ILogPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogPublisher implements ILogPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.audit.exchange}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.audit.routing-key}")
    private String ROUTING_KEY;

    @Override
    public void publish(AuditLog log) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(log);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
    }
}
