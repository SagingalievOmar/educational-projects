package org.example.taskflow.repositories.JPARepositories;

import jakarta.validation.constraints.Email;
import org.example.taskflow.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Page<User> findUserByProjectsId(Long projectId, Pageable pageable);

    boolean existsByEmail(@Email String email);
}
