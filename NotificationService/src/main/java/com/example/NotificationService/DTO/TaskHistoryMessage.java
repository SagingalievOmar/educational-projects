package com.example.NotificationService.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter

@ToString
@EqualsAndHashCode
public class TaskHistoryMessage {

    private Long taskId;
    private Long performedBy;
    private LocalDateTime timestamp;
    private Map<String, String> details;

}
