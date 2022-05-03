package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static hexlet.code.config.SpringConfigForTest.TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TaskStatusTest {

    private static final String PATH_STATUSES = "/api/statuses";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void reg() throws Exception {
        testUtils.regDefaultUser();
    }

    @Test
    void getAll() throws Exception {
        MockHttpServletResponse response = testUtils.perform(
                        get(PATH_STATUSES).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("firstStatus", "secondStatus");
    }

    @Test
    void createTaskStatus() throws Exception {
        TaskStatusDto testStatus = new TaskStatusDto(
                "Status1"
        );

        testUtils.perform(post(PATH_STATUSES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testStatus)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        MockHttpServletResponse response = testUtils.perform(get(PATH_STATUSES)
                .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(testStatus.getName());
    }

    @Test
    void updateTaskStatus() throws Exception {
        TaskStatusDto testStatus = new TaskStatusDto(
                "Status1"
        );

        testUtils.perform(post(PATH_STATUSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStatus)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        long id = taskStatusRepository.findByName(testStatus.getName()).get().getId();

        TaskStatusDto updateTestStatus = new TaskStatusDto(
                "updateStatus"
        );

        testUtils.perform(put(PATH_STATUSES + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTestStatus)),
                TestUtils.TEST_USER).andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(get(PATH_STATUSES)
                        .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(updateTestStatus.getName());
    }

    @Test
    void deleteTaskStatus() throws Exception {
        TaskStatusDto testStatus = new TaskStatusDto(
                "Status1"
        );

        testUtils.perform(post(PATH_STATUSES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStatus)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        long id = taskStatusRepository.findByName(testStatus.getName()).get().getId();

        testUtils.perform(delete(PATH_STATUSES + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER).andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(get(PATH_STATUSES)
                        .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(testStatus.getName());
    }
}
