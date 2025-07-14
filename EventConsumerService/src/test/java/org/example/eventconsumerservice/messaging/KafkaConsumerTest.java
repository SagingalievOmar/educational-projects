package org.example.eventconsumerservice.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.eventconsumerservice.DTO.EventLogResponse;
import org.example.eventconsumerservice.DTO.messages.LogEntry;
import org.example.eventconsumerservice.entities.EventLog;
import org.example.eventconsumerservice.repositories.EventLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SpringBootTest
@Testcontainers
public class KafkaConsumerTest {

    @Container
    @ServiceConnection
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("apache/kafka-native:latest"));

    @Autowired
    private EventLogRepository eventLogRepository;

    @Autowired
    private KafkaTemplate<String, LogEntry> kafkaTemplate;

    private KafkaConsumer<String, String> consumer;

    private LogEntry logEntry;
    private EventLogResponse eventLogResponse;

    @BeforeEach
    public void setup() {
        logEntry = new LogEntry();
        logEntry.setLevel("INFO");
        logEntry.setMessage("Comment created");
        Map<String, Object> context = new HashMap<>();
        context.put("id", 1);
        context.put("taskId", 1);
        context.put("usrId", 1);
        logEntry.setContext(context);

        EventLog eventLog = new EventLog();
        eventLog.setEventType("created");
        eventLog.setEntityType("Comment");
        eventLog.setPayload(logEntry.getContext());

        eventLogResponse = EventLogResponse.toResponse(eventLog);

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        props.put("group.id", "consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("taskFlow.logs.DLQ"));
    }

    @Test
    public void sendRecordToTopicGetItAndFindItFromRepository() throws InterruptedException {
        kafkaTemplate.send("taskFlow.logs", logEntry);
        Thread.sleep(5000);
        Assertions.assertEquals(
                eventLogResponse,
                EventLogResponse.toResponse(eventLogRepository.findAll().getLast()));
    }

    @Test
    public void sendNotValidRecordAndGetItFromDLQ() {
        logEntry.setMessage("");
        kafkaTemplate.send("taskFlow.logs", logEntry);
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));

        Assertions.assertEquals(1, records.count());
    }
}
