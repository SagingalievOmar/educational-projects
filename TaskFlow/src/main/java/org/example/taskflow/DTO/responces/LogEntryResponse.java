package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.models.RecordModels.LogEntry;

import java.util.Map;

@Getter
@Setter
public class LogEntryResponse {

    private String level;
    private String message;
    private String timestamp;
    private Map<String,Object> context;

    public static LogEntryResponse toResponse(LogEntry logEntry) {
        LogEntryResponse response = new LogEntryResponse();
        response.setLevel(logEntry.getLevel().toString());
        response.setMessage(logEntry.getMessage());
        response.setTimestamp(logEntry.getTimestamp().toString());
        response.setContext(logEntry.getContext());
        return response;
    }

}
