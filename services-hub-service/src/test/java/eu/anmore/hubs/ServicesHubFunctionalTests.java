package eu.anmore.hubs;

import eu.anmore.hubs.registration.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebIntegrationTest(randomPort = true)
public class ServicesHubFunctionalTests extends ServicesHubApplicationTests {

    @Value("${local.server.port}")
    protected Integer serverPort;

    protected RestTemplate testRestTemplate = new TestRestTemplate();

    protected String serverUrl(final String url) {
        return String.format("http://localhost:%d%s", serverPort, url.startsWith("/") ? url : "/" + url);
    }

    protected List<String> getServices() {
        return new ArrayList(Arrays.asList(testRestTemplate.getForObject(serverUrl("/api/v1/registration"), String[].class)));
    }

    protected String register(Registration registartion) {
        ResponseEntity<String> entity = testRestTemplate.postForEntity(serverUrl("/api/v1/registration"), registartion, String.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            LOGGER.error("Can't call service [entity={}]", entity);
            throw new RuntimeException("Exception while calling");
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ServicesHubFunctionalTests.class);

}
