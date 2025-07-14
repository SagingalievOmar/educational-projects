package org.example.taskflow.repositories.ElasticRepositories;

import org.example.taskflow.models.indexes.CommentIndex;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentIndexRepository extends ElasticsearchRepository<CommentIndex, Long> {

    @Query("""
            {
                "bool": {
                  "must": [
                    {
                      "match": {
                        "comment": "?0"
                      }
                    }
                  ],
                  "filter": [
                    {
                      "term": {
                        "taskId": "?1"
                      }
                    }
                  ]
                }
            }
            """)
    List<CommentIndex> search(String query, Long taskId);

    Long countByUserId(Long userId);
}
