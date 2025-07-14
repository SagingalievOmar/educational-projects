package com.example.NotificationService.listenerTest;

import com.example.NotificationService.DTO.TaskHistoryMessage;
import com.example.NotificationService.models.TaskNotification;
import com.example.NotificationService.repositories.TaskNotificationRepository;
import com.example.NotificationService.services.TaskNotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Testcontainers
public class ReceiverTest {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.11-management")
            .withExposedPorts(5673);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TaskNotificationService taskNotificationService;

    @Autowired
    private TaskNotificationRepository taskNotificationRepository;

    private String exchange;
    private String routingKey;
    private TaskHistoryMessage taskHistoryMessage;

    @BeforeEach
    public void setup() {
        exchange = "taskFlow.direct.exchange";
        routingKey = "task.notifications";

        taskHistoryMessage = new TaskHistoryMessage();
        taskHistoryMessage.setTaskId(1L);
        Map<String, String> details = new HashMap<>();
        details.put("status", "TODO");
        details.put("title", "do something");
        taskHistoryMessage.setDetails(details);

        taskNotificationRepository.deleteAll();
    }

    @Test
    public void sendAndGetMessageAndSaveItAndGetIt() throws InterruptedException {
        TaskNotification notification = new TaskNotification();
        notification.setId(1L);
        notification.setTaskId(1L);
        notification.setStatus("TODO");
        notification.setTitle("do something");

        rabbitTemplate.convertAndSend(exchange, routingKey, taskHistoryMessage);
        Thread.sleep(2000);
        TaskNotification expected = taskNotificationService.getTaskNotification(1L);

        Assertions.assertEquals(notification, expected);
    }

}
