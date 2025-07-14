package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.models.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponse {

    private String content;
    private UserResponse user;
    private String createdAt;
    private String updatedAt;

    public static CommentResponse toResponse(Comment comment) {
        LocalDateTime updated = comment.getUpdatedAt();
        CommentResponse response = new CommentResponse();

        response.setContent(comment.getContent());
        response.setUser(UserResponse.toResponse(comment.getUser()));
        response.setCreatedAt(comment.getCreatedAt().toString());
        response.setUpdatedAt(updated != null ? updated.toString() : "not updated");

        return response;
    }
}
