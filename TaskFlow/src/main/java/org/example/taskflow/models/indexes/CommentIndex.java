package org.example.taskflow.models.indexes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter

@Document(indexName = "comment_index")
public class CommentIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String comment;
    private Long taskId;
    private Long userId;
    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

}
