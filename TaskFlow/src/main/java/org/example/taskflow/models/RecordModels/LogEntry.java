package org.example.taskflow.models.RecordModels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.example.taskflow.enums.RecordEnums.LogEntryLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString

@Document(collection = "LogEntry")
public class LogEntry {

    @Id
    private ObjectId id;
    private LogEntryLevel level;
    private String message;
    private LocalDateTime timestamp;
    private Map<String,Object> context;

}
