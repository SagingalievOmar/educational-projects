package org.example.taskflow.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.requests.CommentRequest;
import org.example.taskflow.DTO.responces.CommentResponse;
import org.example.taskflow.models.Comment;
import org.example.taskflow.models.indexes.CommentIndex;
import org.example.taskflow.services.CommentService;
import org.example.taskflow.services.ElasticServices.CommentIndexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentIndexService commentIndexService;

    @PostMapping
    public ResponseEntity<CommentResponse> saveComment(@Valid @RequestBody CommentRequest commentRequest) {
        Comment saved = commentService.create(commentRequest);
        return ResponseEntity.ok(CommentResponse.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        CommentResponse response = CommentResponse.toResponse(commentService.getById(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CommentIndex>> search(
            @RequestParam String query,
            @RequestParam Long id) {
        List<CommentIndex> indexes = commentIndexService.search(query, id);
        return ResponseEntity.ok(indexes);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Long> getStatistics(@RequestParam Long userId) {
        return ResponseEntity.ok(commentIndexService.countByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        Comment updated = commentService.update(id, request);
        return ResponseEntity.ok(CommentResponse.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
