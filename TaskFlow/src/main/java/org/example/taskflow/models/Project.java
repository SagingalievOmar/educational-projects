package org.example.taskflow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.enums.ProjectStatus;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "project")
public class Project extends BaseModel{

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
    private List<User> members;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;

}
