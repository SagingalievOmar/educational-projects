package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.User;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponse {
    private String name;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
    private UserResponse owner;

    public static ProjectResponse toResponse(Project project) {
        LocalDateTime updated = project.getUpdatedAt();
        ProjectResponse response = new ProjectResponse();

        response.name = project.getName();
        response.description = project.getDescription();
        response.status = project.getStatus().toString();
        response.createdAt = project.getCreatedAt().toString();
        response.updatedAt = updated != null ? updated.toString() : "not updated";
        response.owner = UserResponse.toResponse(project.getOwner());

        return response;
    }
}
