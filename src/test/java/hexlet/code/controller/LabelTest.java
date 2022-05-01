package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.LabelDto;
import hexlet.code.repository.LabelRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class LabelTest {

    private static final String PATH_LABELS = "/api/labels";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private LabelRepository labelRepository;

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
                get(PATH_LABELS).contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("firstLabel", "secondLabel");
    }

    @Test
    void createLabels() throws Exception {
        LabelDto testLabelDto = new LabelDto(
                "label1"
        );

        testUtils.perform(post(PATH_LABELS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLabelDto)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        MockHttpServletResponse response = testUtils.perform(get(PATH_LABELS)
                        .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER)
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(testLabelDto.getName());
    }

    @Test
    void updateLabels() throws Exception {
        LabelDto testLabelDto = new LabelDto(
                "label1"
        );

        testUtils.perform(post(PATH_LABELS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLabelDto)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        LabelDto updateTestLabelDto = new LabelDto(
                "updateLabel"
        );

        long id = labelRepository.findByName(testLabelDto.getName()).get().getId();

        testUtils.perform(put(PATH_LABELS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTestLabelDto)),
        TestUtils.TEST_USER).andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(get(PATH_LABELS)
                                .contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains(updateTestLabelDto.getName());
    }

    @Test
    void deleteLabels() throws Exception {
        LabelDto testLabelDto = new LabelDto(
                "label1"
        );

        testUtils.perform(post(PATH_LABELS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testLabelDto)),
                TestUtils.TEST_USER).andExpect(status().isCreated());

        long id = labelRepository.findByName(testLabelDto.getName()).get().getId();

        testUtils.perform(delete(PATH_LABELS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON),
                TestUtils.TEST_USER).andExpect(status().isOk());

        MockHttpServletResponse response = testUtils.perform(get(PATH_LABELS)
                                .contentType(MediaType.APPLICATION_JSON),
                        TestUtils.TEST_USER)
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).doesNotContain(testLabelDto.getName());
    }
}
