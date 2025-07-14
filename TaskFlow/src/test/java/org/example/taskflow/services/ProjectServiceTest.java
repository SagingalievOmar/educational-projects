package org.example.taskflow.services;

import org.example.taskflow.DTO.requests.ProjectRequest;
import org.example.taskflow.models.Project;
import org.example.taskflow.models.User;
import org.example.taskflow.repositories.JPARepositories.ProjectJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectJpaRepository projectRepository;

    @Mock
    UserService userService;

    @InjectMocks
    ProjectService projectService;

    private ProjectRequest mockProjectRequest;
    private Project mockProject;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockProjectRequest = new ProjectRequest(
                "name",
                "descriptioin",
                "COMPLETED",
                1L);

        mockProject = new Project();
        mockProject.setId(1L);

        mockUser = new User();
        mockUser.setId(1L);
    }

    @Test
    public void testCreateProject() {
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(mockUser);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(mockProject);
        Assertions.assertEquals(1L, projectService.create(mockProjectRequest));
    }

    @Test
    public void getProjectTest() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockProject));
        Assertions.assertEquals(mockProject, projectService.getById(1L));
    }
}
