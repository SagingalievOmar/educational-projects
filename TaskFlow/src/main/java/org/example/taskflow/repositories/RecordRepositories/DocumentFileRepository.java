package org.example.taskflow.repositories.RecordRepositories;

import org.bson.types.ObjectId;
import org.example.taskflow.models.RecordModels.DocumentFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentFileRepository extends MongoRepository<DocumentFile, ObjectId> {
    List<DocumentFile> getDocumentFilesByTaskId(Long taskId);
}
