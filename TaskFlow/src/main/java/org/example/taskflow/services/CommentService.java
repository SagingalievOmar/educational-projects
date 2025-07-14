package org.example.taskflow.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.DTO.requests.CommentRequest;
import org.example.taskflow.models.Comment;
import org.example.taskflow.repositories.JPARepositories.CommentJpaRepository;
import org.example.taskflow.services.ElasticServices.CommentIndexService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentJpaRepository commentRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final CommentIndexService commentIndexService;

    @Transactional
    public Comment create(CommentRequest request) {
        Comment comment = new Comment();
        comment.setContent(request.content());
        comment.setUser(userService.getById(request.userId()));
        comment.setTask(taskService.getById(request.taskId()));

        Comment created = commentRepository.save(comment);
        commentIndexService.save(created);
        log.info("Comment created: {}", created);
        return created;
    }

    public Page<Comment> getTaskComments(Long taskId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByTaskId(taskId, pageable);
        log.info("Comments found: {}", comments.getTotalElements());
        return comments;
    }

    public Comment getById(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        log.info("Comment found: {}", comment);
        return comment;
    }

    @Transactional
    public Comment update(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        if (request.content() != null) comment.setContent(request.content());

        Comment updated = commentRepository.save(comment);
        commentIndexService.update(id, updated);
        log.info("Comment updated: {}", updated);
        return updated;
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
        commentIndexService.delete(id);
        log.info("Comment deleted: {}", id);
    }
}
