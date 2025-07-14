package org.example.taskflow.services;

import org.example.taskflow.DTO.requests.TaskRequest;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.Task;
import org.example.taskflow.models.User;
import org.example.taskflow.repositories.JPARepositories.TaskJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskJpaRepository taskRepository;

    @Mock
    ProjectService projectService;

    @Mock
    UserService userService;

    @InjectMocks
    TaskService taskService;

    private TaskRequest mockTaskRequest;
    private Project mockProject;
    private User mockUser;
    private Task mockTask;

    @BeforeEach
    public void setUp() {
        mockTaskRequest = new TaskRequest(
                "title",
                "description",
                "TODO",
                "HIGH",
                "2026-12-22 12:35:45",
                1L,
                1L);

        mockUser = new User();
        mockProject = new Project();
        mockTask = new Task();
        mockTask.setId(1L);
    }

    @Test
    public void createTaskTest() {
        Mockito.when(projectService.getById(Mockito.anyLong())).thenReturn(mockProject);
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(mockUser);
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(mockTask);

        Assertions.assertEquals(1L, taskService.create(mockTaskRequest));
    }

    @Test
    public void getTaskTest() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));

        Assertions.assertEquals(mockTask, taskService.getById(1L));
    }

    @Test
    public void updateTaskTest() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(mockUser);
        Mockito.when(projectService.getById(Mockito.anyLong())).thenReturn(mockProject);
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(mockTask);

        Assertions.assertEquals(1L, taskService.update(1L, mockTaskRequest));
    }

    @Test
    public void deleteTaskTest() {
        Mockito.doNothing().when(taskRepository).deleteById(Mockito.anyLong());
        Assertions.assertDoesNotThrow(() -> taskService.delete(1L));
    }

    @Test
    public void throwExceptionWhenTaskNotFound() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> taskService.getById(1L));
    }
}
