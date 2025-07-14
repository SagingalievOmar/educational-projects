package org.example.taskflow.repositories.RecordRepositories;

import org.bson.types.ObjectId;
import org.example.taskflow.models.RecordModels.TaskHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskHistoryRepository extends MongoRepository<TaskHistory, ObjectId> {
    List<TaskHistory> getTaskHistoriesByTaskId(Long taskId);

    void deleteByTaskId(Long taskId);
}
