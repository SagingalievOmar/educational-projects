package org.example.taskflow.repositories;

import org.example.taskflow.enums.RecordEnums.TaskHistoryAction;
import org.example.taskflow.models.RecordModels.TaskHistory;
import org.example.taskflow.repositories.RecordRepositories.TaskHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class TaskHistoryRepositoryTest {

    @Autowired
    TaskHistoryRepository taskHistoryRepository;

    private final List<TaskHistory> taskHistories = new ArrayList<>();
    private final int amount = 3;

    @BeforeEach
    public void setUp() {
        taskHistoryRepository.deleteAll();
        for (long i = 0, j = 0; i < 40; i++) {
            TaskHistory taskHistory = new TaskHistory();
            taskHistory.setTaskId(i % amount == 0 ? j++ : j);
            taskHistory.setAction(TaskHistoryAction.CREATE);
            taskHistories.add(taskHistory);
        }
        taskHistoryRepository.saveAll(taskHistories);
    }

    @Test
    public void getTaskHistoriesByTaskIdTest() {
        Long findingTaskId = 3L;
        List<TaskHistory> histories = taskHistoryRepository.getTaskHistoriesByTaskId(findingTaskId);
        Assertions.assertEquals(amount, histories.size());
        for (TaskHistory history : histories) {
            Assertions.assertEquals(findingTaskId, history.getTaskId());
        }
    }
}
