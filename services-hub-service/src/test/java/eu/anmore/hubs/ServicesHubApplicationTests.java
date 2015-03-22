package eu.anmore.hubs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ServicesHubApplication.class)
public abstract class ServicesHubApplicationTests {

    protected MockRestServiceServer mockServer;

    @Autowired
    private RestTemplate contextRestTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void shouldLoadSpringContext() {
        assertThat(applicationContext).isNotNull();
    }

    @Before
    public void setUpMockRestServer() {
        this.mockServer = MockRestServiceServer.createServer(contextRestTemplate);
    }

}
