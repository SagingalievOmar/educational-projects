package com.example.NotificationService.services;

import com.example.NotificationService.DTO.TaskHistoryMessage;
import com.example.NotificationService.models.TaskNotification;
import com.example.NotificationService.repositories.TaskNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskNotificationService {

    private final TaskNotificationRepository repository;

    public void saveTaskNotification(TaskHistoryMessage message) {
        Map<String, String> details = message.getDetails();
        TaskNotification notification = new TaskNotification();
        notification.setTaskId(message.getTaskId());
        notification.setTitle(details.get("title"));
        notification.setStatus(details.get("status"));
        repository.save(notification);
    }

    public TaskNotification getTaskNotification(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<TaskNotification> getAllTaskNotification() {
        return repository.findAll();
    }

    public TaskNotification updateTaskNotification(Long id, TaskNotification taskHistory) {
        TaskNotification history = repository.findById(id).orElseThrow();

        if (taskHistory.getTaskId() != null) history.setTaskId(taskHistory.getTaskId());
        if (taskHistory.getTitle() != null) history.setTitle(taskHistory.getTitle());
        if (taskHistory.getStatus() != null) history.setStatus(taskHistory.getStatus());

        return repository.save(history);
    }

    public void deleteTaskNotification(Long id) {
        repository.deleteById(id);
    }
}
