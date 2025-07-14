package org.example.taskflow.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.requests.ProjectRequest;
import org.example.taskflow.DTO.responces.ProjectResponse;
import org.example.taskflow.DTO.responces.TaskResponse;
import org.example.taskflow.DTO.responces.UserResponse;
import org.example.taskflow.enums.ProjectStatus;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.indexes.ProjectIndex;
import org.example.taskflow.services.ElasticServices.ProjectIndexService;
import org.example.taskflow.services.ProjectService;
import org.example.taskflow.services.TaskService;
import org.example.taskflow.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;
    private final ProjectIndexService projectIndexService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        Project created = projectService.create(projectRequest);
        return ResponseEntity.ok(ProjectResponse.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectResponse>> getProjects(Pageable pageable) {
        Page<ProjectResponse> responses = projectService.getProjects(pageable)
                .map(ProjectResponse::toResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        ProjectResponse response = projectService.getProjectResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<Page<UserResponse>> getProjectUsers(
            @PathVariable Long id,
            Pageable pageable) {
        Page<UserResponse> userResponses = userService.getProjectUsers(id, pageable)
                .map(UserResponse::toResponse);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Page<TaskResponse>> getProjectTasks(
            @PathVariable Long id,
            Pageable pageable) {
        Page<TaskResponse> TaskResponses = taskService.getProjectTasks(id, pageable)
                .map(TaskResponse::toResponse);
        return ResponseEntity.ok(TaskResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectIndex>> search(@RequestParam String query) {
        List<ProjectIndex> indexes = projectIndexService.search(query);
        return ResponseEntity.ok(indexes);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Long> getStatistic(@RequestParam String status) {
        return ResponseEntity.ok(
                projectIndexService.countByStatus(
                        ProjectStatus.valueOf(status)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse updated = projectService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/users")
    public ResponseEntity addUserToProject(
            @PathVariable Long id,
            @RequestParam Long userId) {
        projectService.linkProjectAndUser(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.ok().build();
    }
}
