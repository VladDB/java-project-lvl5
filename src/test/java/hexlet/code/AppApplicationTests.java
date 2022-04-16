package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@DBRider
//@DBUnit(alwaysCleanBefore = true)
//@DataSet("users.yml")
class AppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testWelcome() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/welcome"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
	}

	@Test
	void testGetAllUsers() throws Exception {
		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
//		assertThat(response.getContentAsString()).contains("Peter");
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

		assertThat(responsePost.getStatus()).isEqualTo(200);

		MockHttpServletResponse response = mockMvc
				.perform(get("/api/users"))
				.andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(200);
		assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
		assertThat(response.getContentAsString()).contains("Jackson", "Bind", "jl@mail.ru");
		assertThat(response.getContentAsString()).doesNotContain("password");

	}

	@Test
	void contextLoads() {
	}

}
