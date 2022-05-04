package hexlet.code.controller;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
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
public class UserControllerTest {

    private static final String PATH_USERS = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    @Test
    void testGetAllUsers() throws Exception {
        User testUser = new User();
        testUser.setFirstName("Max");
        testUser.setLastName("Maximov");
        testUser.setEmail("max@mail.com");
        testUser.setPassword("password");
        userRepository.save(testUser);

        MockHttpServletResponse response = mockMvc
                .perform(get(PATH_USERS))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Max", "Maximov", "max@mail.com");
        assertThat(response.getContentAsString()).doesNotContain("password");
    }

    @Test
    void testCreateUsers() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(post(PATH_USERS).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Jackson\", "
                                + "\"lastName\": \"Bind\", "
                                + "\"email\": \"jb@mail.ru\", "
                                + "\"password\": \"password1\"}"))
                .andReturn().getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);

        MockHttpServletResponse response = mockMvc
                .perform(get(PATH_USERS))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind", "jb@mail.ru");
    }

    @Test
    void deleteUser() throws Exception {
        testUtils.regDefaultUser();

        MockHttpServletResponse responsePost = mockMvc
                .perform(post(PATH_USERS).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Jackson\", "
                                + "\"lastName\": \"Bind\", "
                                + "\"email\": \"jl@mail.ru\", "
                                + "\"password\": \"password\"}"))
                .andReturn().getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);

        MockHttpServletResponse response = mockMvc
                .perform(get(PATH_USERS))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind", "jl@mail.ru");

        long id = userRepository.findByEmail("jl@mail.ru").get().getId();

        testUtils.perform(delete(PATH_USERS + "/" + id), TestUtils.TEST_USER)
                .andExpect(status().isOk());

        MockHttpServletResponse responseAfterDelete = mockMvc
                .perform(get("/api/users"))
                .andReturn()
                .getResponse();

        assertThat(responseAfterDelete.getStatus()).isEqualTo(200);
        assertThat(responseAfterDelete.getContentAsString()).doesNotContain("Jackson", "Bind", "jl@mail.ru");
    }

    @Test
    void updateUser() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(post(PATH_USERS).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Ivan\", "
                                + "\"lastName\": \"Ivanov\", "
                                + "\"email\": \"II@mail.ru\", "
                                + "\"password\": \"password\"}"))
                .andReturn().getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);

        MockHttpServletResponse response = mockMvc
                .perform(get(PATH_USERS))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Ivan", "Ivanov", "II@mail.ru");

        testUtils.regDefaultUser();

        long id = userRepository.findByEmail("II@mail.ru").get().getId();

        testUtils.perform(
                put(PATH_USERS + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Max\", "
                + "\"lastName\": \"Maximov\", "
                + "\"email\": \"II@mail.ru\", "
                + "\"password\": \"password\"}"),
                TestUtils.TEST_USER)
                .andExpect(status().isOk());

        MockHttpServletResponse responseAfterDelete = mockMvc
                .perform(get(PATH_USERS))
                .andReturn()
                .getResponse();

        assertThat(responseAfterDelete.getStatus()).isEqualTo(200);
        assertThat(responseAfterDelete.getContentAsString()).doesNotContain("Ivan", "Ivanov");
        assertThat(responseAfterDelete.getContentAsString()).contains("Max", "Maximov", "II@mail.ru");
    }
}
