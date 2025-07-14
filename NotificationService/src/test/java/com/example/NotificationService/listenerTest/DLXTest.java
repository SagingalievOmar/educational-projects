package com.example.NotificationService.listenerTest;

import com.example.NotificationService.DTO.TaskHistoryMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Testcontainers
public class DLXTest {

    @Container
    static RabbitMQContainer rabbit = new RabbitMQContainer("rabbitmq:3.11");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getAmqpPort);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private String exchange;
    private String routingKey;
    private TaskHistoryMessage taskHistoryMessage;

    @BeforeEach
    public void setup() {
        exchange = "taskFlow.direct.exchange";
        routingKey = "task.notifications";

        taskHistoryMessage = new TaskHistoryMessage();
        Map<String, String> details = new HashMap<>();
        details.put("status", "TODO");
        details.put("title", "do something");
        taskHistoryMessage.setDetails(details);
    }

    @Test
    void testMessageMovesToDLQ() throws InterruptedException {
        rabbitTemplate.convertAndSend(exchange, routingKey, taskHistoryMessage);

        TaskHistoryMessage dlqMessage = null;
        for (int i = 0; i < 10 && dlqMessage == null; i++) {
            Thread.sleep(10000);
            dlqMessage = (TaskHistoryMessage) rabbitTemplate.receiveAndConvert("dead.letter");
            System.out.println(dlqMessage);
        }

        Assertions.assertNotNull(dlqMessage);
        Assertions.assertEquals(taskHistoryMessage, dlqMessage);
    }
}

