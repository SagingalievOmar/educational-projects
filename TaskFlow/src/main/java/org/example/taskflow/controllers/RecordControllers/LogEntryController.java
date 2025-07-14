package org.example.taskflow.controllers.RecordControllers;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.DTO.responces.LogEntryResponse;
import org.example.taskflow.services.RecordServices.LogEntryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogEntryController {
    private final LogEntryService logEntryService;

    @GetMapping
    public List<LogEntryResponse> getLogEntries() {
        return logEntryService.getLogEntries()
                .stream()
                .map(LogEntryResponse::toResponse)
                .toList();
    }

    @GetMapping("?level={level}")
    public List<LogEntryResponse> getLogEntriesByLevel(@PathVariable String level) {
        return logEntryService.getLogEntriesByLevel(level)
                .stream()
                .map(LogEntryResponse::toResponse).toList();
    }

}
