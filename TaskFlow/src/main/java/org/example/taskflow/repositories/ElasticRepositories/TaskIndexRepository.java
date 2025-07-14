package org.example.taskflow.repositories.ElasticRepositories;

import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.example.taskflow.models.indexes.TaskIndex;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskIndexRepository extends ElasticsearchRepository<TaskIndex, Long> {

    @Query("""
            {
                "bool": {
                  "must": [
                    {
                      "multi_match": {
                        "query": "?0",
                        "fields": ["description", "title"]
                      }
                    }
                  ],
                  "filter": [
                    {
                      "term": {
                        "status": "?1"
                      }
                    },
                    {
                      "term": {
                        "priority": "?2"
                      }
                    }
                  ]
                }
            }
            """)
    List<TaskIndex> search(String query, String status, String priority);

    Long countByPriority(TaskPriority priority);

    Long countByStatus(TaskStatus taskStatus);

}
