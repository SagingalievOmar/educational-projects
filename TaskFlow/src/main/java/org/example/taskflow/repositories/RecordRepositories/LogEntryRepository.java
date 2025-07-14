package org.example.taskflow.repositories.RecordRepositories;

import org.bson.types.ObjectId;
import org.example.taskflow.enums.RecordEnums.LogEntryLevel;
import org.example.taskflow.models.RecordModels.LogEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogEntryRepository extends MongoRepository<LogEntry, ObjectId> {
    List<LogEntry> getLogEntriesByLevel(LogEntryLevel logEntryLevel);
}
