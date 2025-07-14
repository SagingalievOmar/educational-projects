package org.example.eventconsumerservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.example.eventconsumerservice.DTO.messages.LogEntry;
import org.example.eventconsumerservice.services.DeadRecordService;
import org.example.eventconsumerservice.services.EventLogService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class kafkaConsumer {

    private final EventLogService eventLogService;
    private final DeadRecordService deadRecordService;

    @KafkaListener(topics = "taskFlow.logs")
    public void listen(ConsumerRecord<String, LogEntry> record) {
        LogEntry logEntry = record.value();
        if (logEntry.getMessage().isBlank()) {
            throw new ValidateException("Message is empty");
        }
        eventLogService.save(logEntry);
    }

    @KafkaListener(topics = "taskFlow.logs.DLQ")
    public void listenDLQ(ConsumerRecord<String, String> record) {
        deadRecordService.save(record.value());
    }
}
