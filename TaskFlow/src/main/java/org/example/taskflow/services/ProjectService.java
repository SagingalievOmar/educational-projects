package org.example.taskflow.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.DTO.requests.ProjectRequest;
import org.example.taskflow.DTO.responces.ProjectResponse;
import org.example.taskflow.enums.ProjectStatus;
import org.example.taskflow.messaging.RedisMessagePublisher;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.User;
import org.example.taskflow.repositories.JPARepositories.ProjectJpaRepository;
import org.example.taskflow.services.ElasticServices.ProjectIndexService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectJpaRepository projectRepository;
    private final UserService userService;
    private final ProjectIndexService projectIndexService;
    private final RedisMessagePublisher redisMessagePublisher;

    @Transactional
    public Project create(ProjectRequest request) {
        Project project = new Project();
        project.setName(request.name());
        project.setDescription(request.description());
        project.setStatus(ProjectStatus.valueOf(request.status()));
        project.setOwner(userService.getById(request.ownerId()));

        Project saved = projectRepository.save(project);
        projectIndexService.save(saved);
        log.info("Project created: {}", saved);
        return saved;
    }

    public Page<Project> getProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAll(pageable);
        log.info("Projects found: {}", projects.getContent());
        return projects;
    }

    public Project getById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow();
        log.info("Project found: {}", project);
        return project;
    }

    @Cacheable(value = "projects", key = "#id")
    public ProjectResponse getProjectResponse(Long id) {
        Project project = getById(id);
        return ProjectResponse.toResponse(project);
    }

    public Page<Project> getUserProjects(Long userId, Pageable pageable) {
        Page<Project> projects = projectRepository.findProjectByOwnerId(userId, pageable);
        log.info("User projects found: {}", projects.getContent());
        return projects;
    }

    public Page<Project> getOwnerProjects(Long ownerId, Pageable pageable) {
        Page<Project> projects = projectRepository.findProjectByOwnerId(ownerId, pageable);
        log.info("Owner projects found: {}", projects.getContent());
        return projects;
    }

    @Transactional
    @CachePut(value = "projects", key = "#id")
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id).orElseThrow();
        if (request.name() != null) project.setName(request.name());
        if (request.description() != null) project.setDescription(request.description());
        if (request.status() != null) project.setStatus(ProjectStatus.valueOf(request.status()));

        Project updated = projectRepository.save(project);
        projectIndexService.update(id, project);
        redisMessagePublisher.publish("my-channel", "project-updated");
        log.info("Project updated: {}", updated);
        return ProjectResponse.toResponse(updated);
    }

    @Transactional
    public void linkProjectAndUser(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        User user = userService.getById(userId);

        project.getMembers().add(user);
        user.getProjects().add(project);

        projectRepository.save(project);
        userService.saveUser(user);
        log.info("User {} linked to project: {}", userId, projectId);
    }

    @Transactional
    @CacheEvict(value = "projects", key = "#id")
    public void delete(Long id) {
        projectRepository.deleteById(id);
        projectIndexService.delete(id);
        redisMessagePublisher.publish("my-channel", "project-deleted");
        log.info("Project deleted: {}", id);
    }

}
