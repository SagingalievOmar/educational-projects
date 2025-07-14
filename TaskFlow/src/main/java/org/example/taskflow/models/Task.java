package org.example.taskflow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "task")
public class Task extends BaseModel {

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignedUser;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;

}
