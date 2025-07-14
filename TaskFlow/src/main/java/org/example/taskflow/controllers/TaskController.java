package org.example.taskflow.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.requests.TaskRequest;
import org.example.taskflow.DTO.responces.CommentResponse;
import org.example.taskflow.DTO.responces.DocumentFileResponse;
import org.example.taskflow.DTO.responces.TaskHistoryResponse;
import org.example.taskflow.DTO.responces.TaskResponse;
import org.example.taskflow.models.Task;
import org.example.taskflow.models.indexes.TaskIndex;
import org.example.taskflow.services.CommentService;
import org.example.taskflow.services.ElasticServices.TaskIndexService;
import org.example.taskflow.services.RecordServices.DocumentFileService;
import org.example.taskflow.services.RecordServices.TaskHistoryService;
import org.example.taskflow.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final DocumentFileService documentFileService;
    private final TaskHistoryService taskHistoryService;
    private final TaskIndexService taskIndexService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        Task created = taskService.create(taskRequest);
        return ResponseEntity.ok(TaskResponse.toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(Pageable pageable) {
        Page<TaskResponse> responses = taskService.getTasks(pageable)
                .map(TaskResponse::toResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<TaskResponse>> getFilteredTasks(
            @RequestParam String status,
            @RequestParam String priority) {
        List<TaskResponse> response = taskService.getFilteredTasks(status, priority)
                .stream()
                .map(TaskResponse::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable Long id) {
        TaskResponse response = taskService.getTaskResponse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponse>> getTaskComments(@PathVariable Long id, Pageable pageable) {
        Page<CommentResponse> responses = commentService.getTaskComments(id, pageable)
                .map(CommentResponse::toResponse);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskIndex>> search(
            @RequestParam String query,
            @RequestParam String status,
            @RequestParam String priority) {
        List<TaskIndex> indexes = taskIndexService.search(query, status, priority);
        return ResponseEntity.ok(indexes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse updated = taskService.update(id, taskRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/documents")
    public ResponseEntity<String> saveDocumentFile(
            @PathVariable Long id,
            @RequestBody MultipartFile fileRequest) {
        Object savedId = documentFileService.saveDocumentFile(id, fileRequest);
        return ResponseEntity.ok(savedId.toString());
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<DocumentFileResponse>> getDocumentFiles(@PathVariable Long id) {
        List<DocumentFileResponse> fileResponses = documentFileService.getDocumentFilesByTaskId(id);
        return ResponseEntity.ok(fileResponses);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TaskHistoryResponse>> getHistory(@PathVariable Long id) {
        List<TaskHistoryResponse> historyResponses = taskHistoryService.getTaskHistory(id)
                .stream()
                .map(taskHistory -> {
                    TaskHistoryResponse historyResponse = new TaskHistoryResponse();
                    historyResponse.setAction(taskHistory.getAction().toString());
                    historyResponse.setPerformedBy(taskHistory.getPerformedBy());
                    historyResponse.setTimestamp(taskHistory.getTimestamp().toString());
                    historyResponse.setDetails(taskHistory.getDetails());
                    return historyResponse;
                }).toList();
        return ResponseEntity.ok(historyResponses);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Long> getStatistic(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority) {
        if (status != null) {
            return ResponseEntity.ok(taskIndexService.countByStatus(priority));
        }
        if (priority != null) {
            return ResponseEntity.ok(taskIndexService.countByPriority(priority));
        }
        return ResponseEntity.status(404).body(0L);
    }
}
