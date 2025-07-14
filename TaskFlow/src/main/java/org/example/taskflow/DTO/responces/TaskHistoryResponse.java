package org.example.taskflow.DTO.responces;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TaskHistoryResponse {

    private String action;
    private Long performedBy;
    private String timestamp;
    private Map<String, Object> details;

}
