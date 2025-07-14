package org.example.taskflow.models.indexes;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.enums.ProjectStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Getter
@Setter

@Document(indexName = "project_index")
public class ProjectIndex {

    @Id
    private Long id;

    private String name;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Keyword)
    private ProjectStatus status;

}
