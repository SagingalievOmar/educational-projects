package org.example.taskflow.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.taskflow.DTO.responces.TaskResponse;
import org.example.taskflow.enums.TaskPriority;
import org.example.taskflow.enums.TaskStatus;
import org.example.taskflow.models.Task;
import org.example.taskflow.repositories.JPARepositories.TaskJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class TaskControllerTDDTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskJpaRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<Task> tasks = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        int AMOUNT = 5;
        for (long i = 1; i < AMOUNT; ++i) {
            Task task = new Task();
            task.setStatus(TaskStatus.TODO);
            task.setPriority(TaskPriority.HIGH);
            task.setDeadline(LocalDateTime.now().plusDays(i));
            tasks.add(task);
            System.out.println("Task " + i + " created");
        }
        taskRepository.saveAll(tasks);
    }

    @Test
    public void testGetFilteredTasks() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/tasks/filtered?status=TODO&priority=HIGH"))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        List<TaskResponse> responses = objectMapper.readValue(json, new TypeReference<>() {});

        int i = 0;
        for (TaskResponse taskResponse : responses) {
            Assertions.assertEquals("TODO", taskResponse.getStatus());
            i++;
        }
        Assertions.assertFalse(tasks.isEmpty());
    }
}
