package org.example.taskflow.controllers;

import org.example.taskflow.configurationsTest.RMQConfigurationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RMQConfigurationTest.class)
@SpringBootTest
public class RMQIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendMessageToDirectQueueAndReceive() {
        String exchange = "taskFlow.direct.exchange";
        String message = "Hello World";
        String routingKey = "task.notifications";
        String queueName = "task.notifications";

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        String receivedMessage = (String) rabbitTemplate.receiveAndConvert(queueName);

        Assertions.assertEquals(message, receivedMessage);
    }

    @Test
    public void sendMessageToFanoutQueueAndReceive() {
        String exchange = "taskFlow.fanout.exchange";
        String message = "Hello World";
        String queueName = "task.audit.fanout";

        rabbitTemplate.convertAndSend(exchange, "routingKey", message);
        String receivedMessage = (String) rabbitTemplate.receiveAndConvert(queueName);

        Assertions.assertEquals(message, receivedMessage);
    }

    @Test
    public void sendMessageToTopicQueueAndReceive() {
        String exchange = "taskFlow.topic.exchange";
        String message = "Hello World";
        String queueName = "task.notifications.topic";
        String routingKey = "task.notifications";

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        String receivedMessage = (String) rabbitTemplate.receiveAndConvert(queueName);

        Assertions.assertEquals(message, receivedMessage);
    }
}


