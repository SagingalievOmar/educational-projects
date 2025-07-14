package org.example.eventconsumerservice.DTO.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
public class LogEntry {

    private String Level;
    private String Message;
    private LocalDateTime timeStamp;
    private Map<String, Object> context;

}
