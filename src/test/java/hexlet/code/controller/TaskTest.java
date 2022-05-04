package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TaskTest {

    private static final String PATH_TASKS = "/api/tasks";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void registrationAndSetData() throws Exception {
        testUtils.regDefaultUser();

        Label firstTestLabel = new Label();
        firstTestLabel.setName("first test label");
        labelRepository.save(firstTestLabel);

        Label secondTestLabel = new Label();
        secondTestLabel.setName("second test label");
        labelRepository.save(secondTestLabel);

        TaskStatus firstTestStatus = new TaskStatus();
        firstTestStatus.setName("first status");
        taskStatusRepository.save(firstTestStatus);

        TaskStatus secondTestStatus = new TaskStatus();
        secondTestStatus.setName("second status");
        taskStatusRepository.save(secondTestStatus);

        User firstTestUser = new User();
        firstTestUser.setFirstName("Max");
        firstTestUser.setLastName("Maximov");
        firstTestUser.setEmail("max@mail.com");
        firstTestUser.setPassword("password");
        userRepository.save(firstTestUser);

        User secondTestUser = new User();
        secondTestUser.setFirstName("Ivan");
        secondTestUser.setLastName("Ivanov");
        secondTestUser.setEmail("ivan@mail.com");
        secondTestUser.setPassword("password");
        userRepository.save(secondTestUser);

        Task testTask = new Task();
        testTask.setName("first test task");
        testTask.setDescription("task's description");
        testTask.setTaskStatus(firstTestStatus);
        testTask.setAuthor(firstTestUser);
        testTask.setExecutor(secondTestUser);
        testTask.setLabels(List.of(firstTestLabel, secondTestLabel));
        taskRepository.save(testTask);
    }

    @Test
    void getAll() throws Exception {
        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                        .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(
                "first test task",
                "task's description",
                "first status",
                "Max",
                "Ivan",
                "first test label"
        );
    }

    @Test
    void createTask() throws Exception {
        long executorId = userRepository.findByEmail("max@mail.com").get().getId();
        long taskStatusId = taskStatusRepository.findByName("second status").get().getId();
        long labelId = labelRepository.findByName("second test label").get().getId();

        TaskDto testTaskDto = new TaskDto(
                "new test task",
                "Description",
                executorId,
                taskStatusId,
                List.of(labelId)
        );

        testUtils.perform(post(PATH_TASKS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskDto)),
                TestUtils.TEST_USER)
                .andExpect(status().isCreated());

        Task testTask = taskRepository.findByName(testTaskDto.getName()).get();

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS + "/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                        .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(objectMapper.writeValueAsString(testTask));
    }

    @Test
    void updateTask() throws Exception {
        long executorId = userRepository.findByEmail("max@mail.com").get().getId();
        long taskStatusId = taskStatusRepository.findByName("second status").get().getId();
        long labelId = labelRepository.findByName("second test label").get().getId();

        TaskDto updateTaskDto = new TaskDto(
                "update test task",
                "Description",
                executorId,
                taskStatusId,
                List.of(labelId)
        );

        Task currentTask = taskRepository.findByName("first test task").get();

        testUtils.perform(put(PATH_TASKS + "/" + currentTask.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTaskDto)),
                                TestUtils.TEST_USER)
                                .andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS + "/" + currentTask.getId())
                        .contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                        .andExpect(status().isOk()).andReturn().getResponse();

        Task testTask = taskRepository.findByName(updateTaskDto.getName()).get();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(objectMapper.writeValueAsString(testTask));
        assertThat(response.getContentAsString()).doesNotContain(objectMapper.writeValueAsString("first test task"));
    }

    @Test
    void deleteTask() throws Exception {
        Task deleteTask = taskRepository.findByName("first test task").get();

        testUtils.perform(delete(PATH_TASKS + "/" + deleteTask.getId())
                                .contentType(MediaType.APPLICATION_JSON),
                                TestUtils.TEST_USER)
                                .andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                        .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(objectMapper.writeValueAsString(deleteTask));
    }
}
