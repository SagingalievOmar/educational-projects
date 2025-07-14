package org.example.taskflow.DTO.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "name must not be empty")
        String name,
        @NotBlank(message = "last name must not be empty")
        String lastName,
        @Email
        String email,
        Boolean active) {
}
