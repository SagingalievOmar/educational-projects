package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.models.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private String name;
    private String lastName;
    private String email;
    private Boolean active;
    private String createdAt;
    private String updatedAt;

    public static UserResponse toResponse(User user) {
        LocalDateTime updated = user.getUpdatedAt();
        UserResponse userResponse = new UserResponse();

        userResponse.setName(user.getName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setActive(user.isActive());
        userResponse.setCreatedAt(user.getCreatedAt().toString());
        userResponse.setUpdatedAt(updated != null ? updated.toString() : "no updated");

        return userResponse;
    }
}
