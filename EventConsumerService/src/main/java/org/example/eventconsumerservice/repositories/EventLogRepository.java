package org.example.eventconsumerservice.repositories;

import org.bson.types.ObjectId;
import org.example.eventconsumerservice.entities.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLogRepository extends MongoRepository<EventLog, ObjectId> {
}
