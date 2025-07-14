package org.example.taskflow.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.requests.UserRequest;
import org.example.taskflow.DTO.responces.ProjectResponse;
import org.example.taskflow.DTO.responces.TaskResponse;
import org.example.taskflow.DTO.responces.UserResponse;
import org.example.taskflow.models.User;
import org.example.taskflow.services.ProjectService;
import org.example.taskflow.services.TaskService;
import org.example.taskflow.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest) {
        User saved = userService.create(userRequest);
        return ResponseEntity.ok(UserResponse.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(Pageable pageable) {
        Page<UserResponse> responses = userService.getUsers(pageable)
                .map(UserResponse::toResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUserResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<Page<ProjectResponse>> getUserProjects(@PathVariable Long id, Pageable pageable) {
        Page<ProjectResponse> projectResponses = projectService.getUserProjects(id, pageable)
                .map(ProjectResponse::toResponse);
        return ResponseEntity.ok(projectResponses);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Page<TaskResponse>> getUserTasks(@PathVariable Long id, Pageable pageable) {
        Page<TaskResponse> taskResponses = taskService.getUserTasks(id, pageable)
                .map(TaskResponse::toResponse);
        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("/{id}/createdProjects")
    public ResponseEntity<Page<ProjectResponse>> getUserCreatedProjects(@PathVariable Long id, Pageable pageable) {
        Page<ProjectResponse> projectResponses = projectService.getOwnerProjects(id, pageable)
                .map(ProjectResponse::toResponse);
        return ResponseEntity.ok(projectResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        UserResponse updated = userService.update(id, userRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
