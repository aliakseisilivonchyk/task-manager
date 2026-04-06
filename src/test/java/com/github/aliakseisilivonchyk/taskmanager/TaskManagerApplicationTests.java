package com.github.aliakseisilivonchyk.taskmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aliakseisilivonchyk.taskmanager.dto.*;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskPriority;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskStatus;
import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class TaskManagerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private Long userId;

    @Test
    @Order(1)
    @DisplayName("1. Отказ в доступе без токена.")
    void shouldDenyProtectedResourceAccessWhenTokenIsNotProvided() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    @DisplayName("2. Регистрация нового пользователя.")
    void shouldRegisterUserWhenInputIsValid() throws Exception {
        UserRequest userRequest = new UserRequest("testUser", "testUser@email.com",
                "secret", UserRole.USER);

        String userResponse = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        UserResponse userResponseObject = objectMapper.readValue(userResponse, UserResponse.class);
        userId = userResponseObject.id();

        assertNotNull(userResponseObject.id());
        assertEquals("testUser", userResponseObject.username());
        assertEquals("testUser@email.com", userResponseObject.email());
        assertEquals(UserRole.USER, userResponseObject.role());;
    }

    @Test
    @Order(3)
    @DisplayName("3. Логин и получение токена.")
    void shouldLoginAndReturnTokenWhenExistingUserDataIsPassed() throws Exception {
        SignInRequest signInRequest = new SignInRequest("testUser", "secret");

        String signInResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = JsonPath.read(signInResponse, "$.token");
        assertNotNull(jwtToken);
        assertFalse(jwtToken.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("4. Доступ к защищенному ресурсу с токеном.")
    void shouldAccessProtectedResourceWhenValidTokenIsProvided() throws Exception {
        TaskRequest taskRequest = new TaskRequest("testTitle", "testDescription",
                TaskStatus.TODO, TaskPriority.LOW, null);

        String taskResponse = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        TaskResponse taskResponseObject = objectMapper.readValue(taskResponse, TaskResponse.class);

        assertNotNull(taskResponseObject.id());
        assertNotNull(taskResponseObject.createdAt());
        assertNotNull(taskResponseObject.updatedAt());
        assertEquals(userId, taskResponseObject.author());
        assertEquals("testTitle", taskResponseObject.title());
        assertEquals("testDescription", taskResponseObject.description());
        assertEquals(TaskStatus.TODO, taskResponseObject.status());
        assertEquals(TaskPriority.LOW, taskResponseObject.priority());
        assertNull(taskResponseObject.assignee());
    }
}
