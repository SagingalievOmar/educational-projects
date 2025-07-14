package org.example.taskflow.services.RecordServices;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskflow.enums.RecordEnums.LogEntryLevel;
import org.example.taskflow.models.RecordModels.LogEntry;
import org.example.taskflow.repositories.RecordRepositories.LogEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogEntryService {
    private final LogEntryRepository logEntryRepository;

    public List<LogEntry> getLogEntries() {
        List<LogEntry> logEntries = logEntryRepository.findAll();
        log.info("{} log entries found", logEntries.size());
        return logEntries;
    }

    public List<LogEntry> getLogEntriesByLevel(String level) {
        LogEntryLevel logEntryLevel = LogEntryLevel.valueOf(level.toUpperCase());
        List<LogEntry> logEntries = logEntryRepository.getLogEntriesByLevel(logEntryLevel);
        log.info("filtered log entries found: {}", logEntries.size());
        return logEntries;
    }
}
