package com.example.NotificationService.listener;

import com.example.NotificationService.DTO.TaskHistoryMessage;
import com.example.NotificationService.services.TaskNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class Receiver {

    private final TaskNotificationService taskHistoryService;

    @RabbitListener(queues = {
            "task.notifications",
            "task.audit.fanout",
            "task.notifications.topic",
            "task.error.topic"})
    public void receive(TaskHistoryMessage message) throws ValidationException {
        if (message.getTaskId() == null) {
            throw new ValidationException("message is not valid");
        }
        taskHistoryService.saveTaskNotification(message);
    }

}
