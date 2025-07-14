package org.example.taskflow.DTO.requests;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "comment must not be empty")
        String content,
        Long userId,
        Long taskId) {
}
