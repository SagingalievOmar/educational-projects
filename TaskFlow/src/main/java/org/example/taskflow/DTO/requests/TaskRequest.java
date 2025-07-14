package org.example.taskflow.DTO.requests;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
        @NotBlank(message = "task title must not be empty")
        String title,
        @NotBlank(message = "task description must not be empty")
        String description,
        String status,
        String priority,
        String deadline,
        Long projectId,
        Long userId) {
}
