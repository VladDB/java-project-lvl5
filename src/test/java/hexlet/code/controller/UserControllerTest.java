package hexlet.code.controller;

//import com.github.database.rider.core.api.configuration.DBUnit;
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.github.database.rider.core.api.dataset.SeedStrategy;
//import com.github.database.rider.junit5.api.DBRider;
import hexlet.code.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
//@DBRider
//@DBUnit(cacheConnection = false, leakHunter = true)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    @DataSet("users.yml")
//    public void setUpUsers() {
//    }

    @Test
    void testGetAllUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/api/users"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUsers() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"Jackson\", "
                                + "\"lastName\": \"Bind\", "
                                + "\"email\": \"jl@mail.ru\", "
                                + "\"password\": \"password\"}"))
                .andReturn().getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(201);

        MockHttpServletResponse response = mockMvc
                .perform(get("/api/users"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind", "jl@mail.ru");
        assertThat(response.getContentAsString()).doesNotContain("password");
    }
// добавить авторизацию
//    @Test
//    void deleteUser() throws Exception {
//        MockHttpServletResponse responsePost = mockMvc
//                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"firstName\": \"Jackson\", "
//                                + "\"lastName\": \"Bind\", "
//                                + "\"email\": \"jl@mail.ru\", "
//                                + "\"password\": \"password\"}"))
//                .andReturn().getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
//        assertThat(response.getContentAsString()).contains("Jackson", "Bind", "jl@mail.ru");
//
//        MockHttpServletResponse responseDelete = mockMvc
//                .perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//        MockHttpServletResponse responseAfterDelete = mockMvc
//                .perform(get("/api/users"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(responseAfterDelete.getStatus()).isEqualTo(200);
//        assertThat(responseAfterDelete.getContentAsString()).doesNotContain("Jackson", "Bind", "jl@mail.ru");
//    }
}
