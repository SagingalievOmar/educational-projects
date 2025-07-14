package org.example.taskflow.repositories.JPARepositories;

import org.example.taskflow.models.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    Page<Project> findProjectByOwnerId(Long id, Pageable pageable);
}
