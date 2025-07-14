package org.example.eventconsumerservice.DTO;

import lombok.Getter;
import lombok.Setter;
import org.example.eventconsumerservice.entities.DeadRecord;

@Getter
@Setter
public class DeadRecordResponse {
    private String data;

    public static DeadRecordResponse toResponse(DeadRecord deadRecord) {
        DeadRecordResponse response = new DeadRecordResponse();
        response.setData(deadRecord.getData());
        return response;
    }
}
