package org.example.eventconsumerservice.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.eventconsumerservice.entities.EventLog;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventLogResponse {

    private String EventType;
    private String EntityType;
    private LocalDateTime createdAt;

    public static EventLogResponse toResponse(EventLog eventLog) {
        EventLogResponse response = new EventLogResponse();
        response.setEventType(eventLog.getEventType());
        response.setEntityType(eventLog.getEntityType());
        response.setCreatedAt(eventLog.getCreatedAt());
        return response;
    }
}
