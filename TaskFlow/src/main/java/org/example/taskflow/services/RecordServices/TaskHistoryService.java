package org.example.taskflow.services.RecordServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.enums.RecordEnums.TaskHistoryAction;
import org.example.taskflow.models.RecordModels.TaskHistory;
import org.example.taskflow.models.Task;
import org.example.taskflow.repositories.RecordRepositories.TaskHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskHistoryService {
    private final TaskHistoryRepository taskHistoryRepository;

    public List<TaskHistory> getTaskHistory(Long taskId) {
        List<TaskHistory> taskHistories = taskHistoryRepository.getTaskHistoriesByTaskId(taskId);
        log.info("Found {} taskHistories", taskHistories.size());
        return taskHistories;
    }

    public void saveTaskHistory(Task task, TaskHistoryAction action) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskId(task.getId());
        taskHistory.setAction(action);
        taskHistory.setTimestamp(LocalDateTime.now());

        Map<String, Object> details = new HashMap<>();
        details.put("title", task.getTitle());
        details.put("description", task.getDescription());
        details.put("status", task.getStatus());
        taskHistory.setDetails(details);
        taskHistoryRepository.save(taskHistory);

    }

    public void saveTaskHistory(Long taskId, TaskHistoryAction action) {
        TaskHistory taskHistory = new TaskHistory();
        taskHistory.setTaskId(taskId);
        taskHistory.setAction(action);
        taskHistory.setTimestamp(LocalDateTime.now());
        taskHistoryRepository.save(taskHistory);

    }

    public void deleteTaskHistory(Long taskId) {
        taskHistoryRepository.deleteByTaskId(taskId);
    }


}
