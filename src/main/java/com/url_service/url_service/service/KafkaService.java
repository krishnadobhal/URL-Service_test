package com.url_service.url_service.service;

import com.url_service.url_service.dto.AnalyticMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaTemplate<String, AnalyticMessage> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, AnalyticMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AnalyticMessage message) {
        kafkaTemplate.send("analytics", message);
        log.info("Sent Message: {}", message);
    }
}