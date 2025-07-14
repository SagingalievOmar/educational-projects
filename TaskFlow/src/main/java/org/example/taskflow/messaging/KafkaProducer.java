package org.example.taskflow.messaging;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.messages.LogEntryMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, LogEntryMessage> kafkaTemplate;

    public void send(String topic, LogEntryMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
