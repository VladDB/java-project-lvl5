package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
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
    private ObjectMapper objectMapper;

    @BeforeEach
    public void reg() throws Exception {
        testUtils.regDefaultUser();
    }

    @AfterEach
    public void clean() {
        testUtils.tearDown();
    }

    @Test
    void getAll() throws Exception {
        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("firstStatus", "secondStatus");
    }

    @Test
    void createTask() throws Exception {
        List<Long> labels = List.of(12L, 13L);
        TaskDto testTaskDto = new TaskDto(
                "third Task",
                "Description",
                10,
                14,
                labels
        );

        testUtils.perform(post(PATH_TASKS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTaskDto)),
                TestUtils.TEST_USER)
                .andExpect(status().isCreated());

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        Task testTask = taskRepository.findByName(testTaskDto.getName()).get();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(objectMapper.writeValueAsString(testTask));
    }

    @Test
    void updateTask() throws Exception {
        List<Long> labels = List.of(12L, 13L);
        TaskDto updateTaskDto = new TaskDto(
                "third Task",
                "Description",
                10,
                14,
                labels
        );

        Task currentTask = taskRepository.findByName("firstTask").get();

        testUtils.perform(put(PATH_TASKS + "/" + currentTask.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateTaskDto)),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        Task testTask = taskRepository.findByName(updateTaskDto.getName()).get();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(objectMapper.writeValueAsString(testTask));
        assertThat(response.getContentAsString()).doesNotContain(objectMapper.writeValueAsString("firstTask"));
    }

    @Test
    void deleteTask() throws Exception {
        Task deleteTask = taskRepository.findByName("firstTask").get();

        testUtils.perform(delete(PATH_TASKS + "/" + deleteTask.getId())
                                .contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_TASKS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(objectMapper.writeValueAsString("firstTask"));
    }
}
