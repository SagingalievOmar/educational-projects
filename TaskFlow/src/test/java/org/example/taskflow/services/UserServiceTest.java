package org.example.taskflow.services;

import org.example.taskflow.DTO.requests.UserRequest;
import org.example.taskflow.models.User;
import org.example.taskflow.repositories.JPARepositories.UserJpaRepository;
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
public class UserServiceTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRequest mockUserRequest;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        mockUserRequest = new UserRequest("name", "lastName", "email", true);
        mockUser = new User();
        mockUser.setId(1L);
    }

    @Test
    public void createUserTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(mockUser);
        Assertions.assertEquals(1L, userService.create(mockUserRequest));
    }

    @Test
    public void getUserTest() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockUser));
        Assertions.assertEquals(mockUser, userService.getById(1L));
    }
}
