package org.example.taskflow.services.ElasticServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.enums.ProjectStatus;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.indexes.ProjectIndex;
import org.example.taskflow.repositories.ElasticRepositories.ProjectIndexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectIndexService {

    private final ProjectIndexRepository repository;

    public ProjectIndex save(Project project) {
        ProjectIndex index = new ProjectIndex();
        index.setDescription(project.getDescription());
        index.setName(project.getName());
        index.setStatus(project.getStatus());
        ProjectIndex saved = repository.save(index);
        log.info("ProjectIndex saved: {}", saved);
        return saved;
    }

    public ProjectIndex getById(Long id) {
        ProjectIndex index = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("ProjectIndex with id %s not found", id)));
        log.info("ProjectIndex found: {}", index);
        return index;
    }

    public List<ProjectIndex> search(String query) {
        List<ProjectIndex> indexes = repository.search(query);
        log.info("ProjectIndexes found: {}", indexes.size());
        return indexes;
    }

    public ProjectIndex update(Long id, Project project) {
        ProjectIndex existing = getById(id);
        if (project.getDescription() != null) existing.setDescription(project.getDescription());
        if (project.getName() != null) existing.setName(project.getName());
        if (project.getStatus() != null) existing.setStatus(project.getStatus());
        ProjectIndex updated = repository.save(existing);
        log.info("ProjectIndex updated: {}", updated);
        return updated;
    }

    public Long countByStatus(ProjectStatus status) {
        Long value = repository.countByStatus(status);
        log.info("ProjectIndex countByStatus found: {}", value);
        return value;
    }

    public void delete(Long id) {
        repository.deleteById(id);
        log.info("ProjectIndex deleted: {}", id);
    }
}
