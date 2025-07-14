package org.example.taskflow.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.DTO.messages.TaskHistoryMessage;
import org.example.taskflow.models.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RMQProducer {
    private final RabbitTemplate template;

    public void sendMessage(Task task, String exchange, String routingKey) {
        Map<String, Object> details = new HashMap<>();
        details.put("title", task.getTitle());
        details.put("status", task.getStatus().toString());

        TaskHistoryMessage taskHistoryMessage = new TaskHistoryMessage(
                task.getId(),
                null,
                LocalDateTime.now(),
                details);
        template.convertAndSend(exchange, routingKey, taskHistoryMessage);
        log.info("Message {} sent to exchange {}, with routing key {}", task.getId(), exchange, routingKey);
    }

    public void sendMessage(Long taskId, String exchange, String routingKey) {
        TaskHistoryMessage taskHistoryMessage = new TaskHistoryMessage(
                taskId,
                null,
                LocalDateTime.now(),
                null);
        template.convertAndSend(exchange, routingKey, taskHistoryMessage);
        log.info("Message {} sent to exchange {}", taskId, exchange);
    }
}
