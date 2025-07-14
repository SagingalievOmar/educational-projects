package org.example.taskflow.models.RecordModels;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.example.taskflow.enums.RecordEnums.TaskHistoryAction;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter

@Document(collection = "TaskHistory")
public class TaskHistory {

    @Id
    private ObjectId id;
    private Long taskId;
    private TaskHistoryAction action;
    private Long performedBy;
    private LocalDateTime timestamp;
    private Map<String, Object> details;

}
