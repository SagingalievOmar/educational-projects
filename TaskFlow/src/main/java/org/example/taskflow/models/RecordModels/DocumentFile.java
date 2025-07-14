package org.example.taskflow.models.RecordModels;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter

@Document(collection = "DocumentFile")
public class DocumentFile {

    @Id
    private ObjectId id;
    private Long taskId;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadedAt;
    private Long size;

}
