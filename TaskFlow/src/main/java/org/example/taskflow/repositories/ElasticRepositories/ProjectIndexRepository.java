package org.example.taskflow.repositories.ElasticRepositories;

import org.example.taskflow.enums.ProjectStatus;
import org.example.taskflow.models.indexes.ProjectIndex;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectIndexRepository extends ElasticsearchRepository<ProjectIndex, Long> {

    @Query("""
            {
              "match": {
                "description": "?0"
              }
            }
            """)
    List<ProjectIndex> search(String query);

    Long countByStatus(ProjectStatus status);

}
