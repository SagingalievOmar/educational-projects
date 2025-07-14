package org.example.taskflow.services.ElasticServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.example.taskflow.models.Task;
import org.example.taskflow.models.indexes.TaskIndex;
import org.example.taskflow.repositories.ElasticRepositories.TaskIndexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskIndexService {

    private final TaskIndexRepository repository;

    public TaskIndex save(Task task) {
        TaskIndex index = new TaskIndex();
        index.setTitle(task.getTitle());
        index.setDescription(task.getDescription());
        index.setDeadline(task.getDeadline());
        index.setPriority(task.getPriority());
        index.setUserId(task.getAssignedUser().getId());
        index.setProjectId(task.getProject().getId());
        TaskIndex saved = repository.save(index);
        log.info("TaskIndex saved: {}", saved);
        return saved;
    }

    public TaskIndex getById(Long id) {
        TaskIndex found = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("TaskIndex with id '%s' not found", id)));
        log.info("TaskIndex found: {}", found);
        return found;
    }

    public List<TaskIndex> search(String query, String status, String priority) {
        List<TaskIndex> indexes = repository.search(query, status, priority);
        log.info("TaskIndexes found: {}", indexes.size());
        return indexes;
    }

    public TaskIndex update(Long id, Task task) {
        TaskIndex existing = getById(id);
        if (task.getDeadline() != null) existing.setDeadline(task.getDeadline());
        if (task.getDescription() != null) existing.setDescription(task.getDescription());
        if (task.getStatus() != null) existing.setStatus(task.getStatus());
        if (task.getPriority() != null) existing.setPriority(task.getPriority());
        if (task.getTitle() != null) existing.setTitle(task.getTitle());
        TaskIndex updated = repository.save(existing);
        log.info("TaskIndex updated: {}", updated);
        return updated;
    }

    public Long countByPriority(String priority) {
        Long value = repository.countByPriority(TaskPriority.valueOf(priority));
        log.info("TaskIndex countByPriority: {}", value);
        return value;
    }

    public Long countByStatus(String status) {
        Long value = repository.countByStatus(TaskStatus.valueOf(status));
        log.info("TaskIndex countByStatus: {}", value);
        return value;
    }

    public void delete(Long id) {
        repository.deleteById(id);
        log.info("TaskIndex deleted: {}", id);
    }
}
