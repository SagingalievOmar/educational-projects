package org.example.taskflow.repositories.JPARepositories;

import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.example.taskflow.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Long> {
    Page<Task> findTasksByProjectId(Long projectId, Pageable pageable);
    Page<Task> findTasksByAssignedUserId(Long userId, Pageable pageable);

    List<Task> findAllByStatusAndPriority(TaskStatus taskStatus, TaskPriority taskPriority);
}
