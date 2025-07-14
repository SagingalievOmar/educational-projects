package org.example.eventconsumerservice.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString

@Document
public class EventLog {

    @Id
    private ObjectId id;
    private String EventType;
    private String EntityType;
    private LocalDateTime createdAt;
    private Map<String, Object> payload;

}
