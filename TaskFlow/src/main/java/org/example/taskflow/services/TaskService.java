package org.example.taskflow.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.DTO.requests.TaskRequest;
import org.example.taskflow.DTO.responces.TaskResponse;
import org.example.taskflow.enums.RecordEnums.TaskHistoryAction;
import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.example.taskflow.messaging.RMQProducer;
import org.example.taskflow.messaging.RedisMessagePublisher;
import org.example.taskflow.models.Task;
import org.example.taskflow.repositories.JPARepositories.TaskJpaRepository;
import org.example.taskflow.services.ElasticServices.TaskIndexService;
import org.example.taskflow.services.RecordServices.TaskHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskJpaRepository taskRepository;
    private final UserService userService;
    private final ProjectService projectService;
    private final TaskHistoryService taskHistoryService;
    private final TaskIndexService taskIndexService;
    private final RMQProducer rmqService;
    private final RedisMessagePublisher redisMessagePublisher;

    @Value("${exchanges.direct}")
    private String directExchange;

    @Value("${exchanges.fanout}")
    private String fanoutExchange;

    @Value("${exchanges.topic}")
    private String topicExchange;

    @Transactional
    public Task create(TaskRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.valueOf(request.status()));
        task.setPriority(TaskPriority.valueOf(request.priority()));
        task.setDeadline(LocalDateTime.parse(request.deadline(), formatter));
        task.setAssignedUser(userService.getById(request.userId()));
        task.setProject(projectService.getById(request.projectId()));

        Task created = taskRepository.save(task);
        rmqService.sendMessage(created, directExchange, "task.notifications");
        taskHistoryService.saveTaskHistory(created, TaskHistoryAction.CREATE);
        taskIndexService.save(created);

        log.info("Task created: {}", created);
        return created;
    }

    public Page<Task> getTasks(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        log.info("Tasks found: {}", tasks.getTotalElements());
        return tasks;
    }

    public List<Task> getFilteredTasks(String status, String priority) {
        List<Task> tasks = taskRepository.findAllByStatusAndPriority(
                TaskStatus.valueOf(status),
                TaskPriority.valueOf(priority));
        log.info("filtered tasks found: {}", tasks.size());
        return tasks;
    }

    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        log.info("Task found: {}", task);
        return task;
    }

    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse getTaskResponse(Long id) {
        Task task = getById(id);
        return TaskResponse.toResponse(task);
    }

    public Page<Task> getUserTasks(Long userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findTasksByAssignedUserId(userId, pageable);
        log.info("User tasks found: {}", tasks.getTotalElements());
        return tasks;
    }

    public Page<Task> getProjectTasks(Long projectId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findTasksByProjectId(projectId, pageable);
        log.info("Project tasks found: {}", tasks.getTotalElements());
        return tasks;
    }

    @Transactional
    @CachePut(value = "tasks", key = "#id")
    public TaskResponse update(Long id, TaskRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Task task = taskRepository.findById(id).orElseThrow();

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.status() != null) task.setStatus(TaskStatus.valueOf(request.status()));
        if (request.priority() != null) task.setPriority(TaskPriority.valueOf(request.priority()));
        if (request.deadline() != null) task.setDeadline(LocalDateTime.parse(request.deadline(), formatter));
        if (request.projectId() != null) task.setProject(projectService.getById(request.projectId()));
        if (request.userId() != null) task.setAssignedUser(userService.getById(request.userId()));

        Task updated = taskRepository.save(task);
        rmqService.sendMessage(updated, fanoutExchange, "task.notifications");
        taskHistoryService.saveTaskHistory(updated, TaskHistoryAction.UPDATE);
        taskIndexService.update(id, updated);
        log.info("Task updated: {}", updated);
        redisMessagePublisher.publish("my-channel", "task-updated");
        return TaskResponse.toResponse(updated);
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
        taskIndexService.delete(id);
        log.info("Task deleted: {}", id);
        rmqService.sendMessage(
                id,
                topicExchange,
                "task.notifications");
        taskHistoryService.saveTaskHistory(id, TaskHistoryAction.DELETE);
        redisMessagePublisher.publish("my-channel", "task-deleted");
    }
}
