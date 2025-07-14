package org.example.eventconsumerservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventconsumerservice.DTO.DeadRecordResponse;
import org.example.eventconsumerservice.DTO.EventLogResponse;
import org.example.eventconsumerservice.services.DeadRecordService;
import org.example.eventconsumerservice.services.EventLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventLogController {

    private final EventLogService service;
    private final DeadRecordService deadRecordService;

    @GetMapping("/processed-events")
    public ResponseEntity<List<EventLogResponse>> getAll() {
        List<EventLogResponse> responses = service.findAll()
                .stream()
                .map(EventLogResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/errors")
    public ResponseEntity<List<DeadRecordResponse>> getAllErrors() {
        List<DeadRecordResponse> responses = deadRecordService.getDeadRecords()
                .stream()
                .map(DeadRecordResponse::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
