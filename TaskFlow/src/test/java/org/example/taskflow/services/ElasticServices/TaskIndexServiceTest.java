package org.example.taskflow.services.ElasticServices;

import org.example.taskflow.models.Project;
import org.example.taskflow.models.Task;
import org.example.taskflow.models.User;
import org.example.taskflow.models.indexes.TaskIndex;
import org.example.taskflow.repositories.ElasticRepositories.TaskIndexRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TaskIndexServiceTest {

    @Autowired
    private TaskIndexService taskIndexService;

    @Autowired
    private TaskIndexRepository taskIndexRepository;

    @Test
    public void indexingTaskTest() {
        Project project = new Project();
        project.setId(10L);
        User user = new User();
        user.setId(10L);

        Task task = new Task();
        task.setId(10L);
        task.setProject(project);
        task.setAssignedUser(user);

        TaskIndex taskIndex = new TaskIndex();
        taskIndex.setProjectId(project.getId());
        taskIndex.setUserId(user.getId());

        taskIndexService.save(task);
        TaskIndex index = taskIndexRepository.findById(task.getId()).orElse(null);
        assert index != null;
        index.setId(null);
        Assertions.assertEquals(taskIndex, index);
    }

    @Test
    public void searchTaskIndexesTest() {
        List<TaskIndex> indexes = taskIndexService.search("login", "TODO", "HIGH");
        Assertions.assertNotNull(indexes);
    }

    @Test
    public void getTaskIndexesStatisticsTest() {
        Long value = taskIndexService.countByPriority("HIGH");
        Assertions.assertTrue(value > 0);
    }
}
