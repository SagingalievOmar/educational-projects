package org.example.eventconsumerservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.example.eventconsumerservice.DTO.messages.LogEntry;
import org.example.eventconsumerservice.entities.EventLog;
import org.example.eventconsumerservice.repositories.EventLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventLogService {

    private final EventLogRepository eventLogRespository;

    public void save(LogEntry logEntry) {
        String[] details = logEntry.getMessage().split(" ");

        EventLog eventLog = new EventLog();
        eventLog.setEventType(details[1]);
        eventLog.setEntityType(details[0]);
        eventLog.setCreatedAt(logEntry.getTimeStamp());
        eventLog.setPayload(logEntry.getContext());
        System.out.println(eventLog);
        if (details[0].equals("Task")) {
            log.info(logEntry.toString());
        } else eventLogRespository.save(eventLog);
    }

    public List<EventLog> findAll() {
        return eventLogRespository.findAll();
    }

    public EventLog findById(ObjectId id) {
        return eventLogRespository.findById(id).orElse(null);
    }

}
