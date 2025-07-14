package org.example.taskflow.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.DTO.requests.UserRequest;
import org.example.taskflow.DTO.responces.UserResponse;
import org.example.taskflow.messaging.RedisMessagePublisher;
import org.example.taskflow.models.User;
import org.example.taskflow.repositories.JPARepositories.UserJpaRepository;
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
public class UserService {

    private final UserJpaRepository userRepository;
    private final RedisMessagePublisher redisMessagePublisher;

    @Transactional
    public User create(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setActive(request.active());

        User created = userRepository.save(user);
        log.info("User created: {}", created);
        return created;
    }

    public void saveUser(User user) {
        userRepository.save(user);
        log.info("User saved: {}", user.getId());
    }

    public Page<User> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Found {} users", users.getTotalElements());
        return users;
    }


    public User getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        log.info("Found user: {}", user);
        return user;
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserResponse(Long id) {
        User user = getById(id);
        return UserResponse.toResponse(user);
    }

    public Page<User> getProjectUsers(Long projectId, Pageable pageable) {
        Page<User> users = userRepository.findUserByProjectsId(projectId, pageable);
        log.info("Found project {} users", users.getTotalElements());
        return users;
    }

    @Transactional
    @CachePut(value = "users", key = "#id")
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow();

        if (request.name() != null) user.setName(request.name());
        if (request.lastName() != null) user.setLastName(request.lastName());
        if (request.email() != null) user.setEmail(request.email());

        User updated = userRepository.save(user);
        log.info("User updated: {}", updated);
        redisMessagePublisher.publish("my-channel", "user-updated");
        return UserResponse.toResponse(updated);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("User deleted: {}", id);
        redisMessagePublisher.publish("my-channel", "user-deleted");
    }
}
