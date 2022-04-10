package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
	void contextLoads() {
	}

}
