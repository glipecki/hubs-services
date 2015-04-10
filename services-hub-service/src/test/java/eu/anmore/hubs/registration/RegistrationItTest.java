package eu.anmore.hubs.registration;

import eu.anmore.hubs.ServicesHubIntegrationTests;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationItTest extends ServicesHubIntegrationTests {

    @Test
    public void shouldRegisterServiceEndpoint() throws Exception {
        register(new Registration("sample-url", Arrays.asList(new ServiceRegistration("sample-service", "1.0"))));

        assertThat(getServiceNames()).hasSize(1).contains("sample-service");
    }

    @Test
    public void shouldNotDuplicateServiesOnList() throws Exception {
        register(new Registration("sample-url-1", Arrays.asList(new ServiceRegistration("sample-service", "1.0"))));
        register(new Registration("sample-url-2", Arrays.asList(new ServiceRegistration("sample-service", "1.0"))));

        assertThat(getServiceNames()).hasSize(1).contains("sample-service");
    }

    private List<String> getServiceNames() throws Exception {
        return getServices().parallelStream().map(s -> s.getServiceName()).collect(Collectors.toList());
    }

}
