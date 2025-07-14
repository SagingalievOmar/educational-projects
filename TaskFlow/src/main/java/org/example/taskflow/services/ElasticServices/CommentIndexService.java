package org.example.taskflow.services.ElasticServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.models.Comment;
import org.example.taskflow.models.indexes.CommentIndex;
import org.example.taskflow.repositories.ElasticRepositories.CommentIndexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentIndexService {

    private final CommentIndexRepository repository;

    public CommentIndex save(Comment comment) {
        CommentIndex index = new CommentIndex();
        index.setComment(comment.getContent());
        index.setUserId(comment.getUser().getId());
        index.setTaskId(comment.getTask().getId());
        CommentIndex saved = repository.save(index);
        log.info("CommentIndex saved: {}", saved);
        return saved;
    }

    public CommentIndex getById(Long id) {
        CommentIndex index = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Could not find comment index with id: %s", id)));
        log.info("CommentIndex getById: {}", index);
        return index;
    }

    public List<CommentIndex> search(String query, Long taskId) {
        List<CommentIndex> indexes = repository.search(query, taskId);
        log.info("CommentIndexes search: {}", indexes);
        return indexes;
    }

    public CommentIndex update(Long id, Comment comment) {
        CommentIndex existing = getById(id);
        if (comment.getContent() != null) existing.setComment(comment.getContent());
        CommentIndex updated = repository.save(existing);
        log.info("CommentIndex updated: {}", updated);
        return updated;
    }

    public Long countByUserId(Long userId) {
        Long value = repository.countByUserId(userId);
        log.info("CommentIndex countByUserId: {}", value);
        return value;
    }

    public void delete(Long id) {
        repository.deleteById(id);
        log.info("CommentIndex deleted: {}", id);
    }
}
