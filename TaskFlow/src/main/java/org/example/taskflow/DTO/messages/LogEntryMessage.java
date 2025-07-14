package org.example.taskflow.DTO.messages;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.enums.RecordEnums.LogEntryLevel;
import org.example.taskflow.models.RecordModels.LogEntry;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class LogEntryMessage {

    private LogEntryLevel level;
    private String message;
    private LocalDateTime timestamp;
    private Map<String,Object> context;

    public static LogEntryMessage toMessage(LogEntry logEntry) {
        LogEntryMessage message = new LogEntryMessage();
        message.level = logEntry.getLevel();
        message.message = logEntry.getMessage();
        message.timestamp = logEntry.getTimestamp();
        message.context = logEntry.getContext();
        return message;
    }
}
