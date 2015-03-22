package eu.anmore.hubs;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.anmore.hubs.registration.Registration;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebAppConfiguration
@Transactional
public class ServicesHubIntegrationTests extends ServicesHubApplicationTests {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    protected List<String> getServices() throws Exception {
        String result = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/registration"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return new ArrayList(Arrays.asList(new ObjectMapper().readValue(result, String[].class)));
    }

    protected String register(Registration registartion) throws Exception {
        return mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registartion)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

}
