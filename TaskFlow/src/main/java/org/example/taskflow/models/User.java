package org.example.taskflow.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User extends BaseModel {

    private String name;
    private String lastName;
    private String email;
    private boolean active;

    @OneToMany(mappedBy = "assignedUser")
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner")
    private List<Project> createdProjects;

    @ManyToMany(mappedBy = "members")
    private Set<Project> projects;

}
