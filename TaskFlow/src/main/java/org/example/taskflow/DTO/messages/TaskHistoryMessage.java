package org.example.taskflow.DTO.messages;

import java.time.LocalDateTime;
import java.util.Map;

public record TaskHistoryMessage (
        Long taskId,
        Long performedBy,
        LocalDateTime timestamp,
        Map<String, Object> details) {}
