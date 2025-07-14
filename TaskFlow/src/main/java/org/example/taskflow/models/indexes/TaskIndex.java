package org.example.taskflow.models.indexes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString

@Document(indexName = "task_index")
public class TaskIndex {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Keyword)
    private TaskStatus status;
    @Field(type = FieldType.Keyword)
    private TaskPriority priority;
    @Field(type = FieldType.Date)
    private LocalDateTime deadline;
    private Long userId;
    private Long projectId;

}
