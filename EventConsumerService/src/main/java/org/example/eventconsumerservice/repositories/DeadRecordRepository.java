package org.example.eventconsumerservice.repositories;

import org.bson.types.ObjectId;
import org.example.eventconsumerservice.entities.DeadRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeadRecordRepository extends MongoRepository<DeadRecord, ObjectId> {
}
