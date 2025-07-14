package org.example.taskflow.DTO.requests;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "empty title")
        String name,
        @NotBlank(message = "empty description")
        String description,
        String status,
        Long ownerId) {
}
