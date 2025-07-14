package org.example.taskflow.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void NoSuchElementExceptionTest() throws Exception {
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/users/44"))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    public void BadRequestExceptionTest() throws Exception {
        String userJson = """
        {
          "name": "John",
          "lastName": "Doe",
          "email": "notValidEmail",
          "active": true
        }
        """;
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

}
