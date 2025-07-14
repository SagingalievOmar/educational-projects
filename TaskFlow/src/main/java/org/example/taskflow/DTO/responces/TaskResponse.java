package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.models.Task;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponse {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String deadline;
    private String createdAt;
    private String updatedAt;

    public static TaskResponse toResponse(Task task) {
        LocalDateTime updated = task.getUpdatedAt();
        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus().toString());
        taskResponse.setPriority(task.getPriority().toString());
        taskResponse.setDeadline(task.getDeadline().toString());
        taskResponse.setCreatedAt(task.getCreatedAt().toString());
        taskResponse.setUpdatedAt(updated != null ? updated.toString() : "not updated");

        return taskResponse;
    }
}
