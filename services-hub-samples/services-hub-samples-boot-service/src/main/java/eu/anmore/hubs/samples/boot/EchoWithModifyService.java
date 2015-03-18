package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.api.spring.JsonServiceAdapter;
import eu.anmore.hubs.registration.ServiceRegistration;
import org.springframework.stereotype.Component;

@Component
public class EchoWithModifyService extends JsonServiceAdapter<String, String> {

    @Override
    public ServiceRegistration getServiceRegistration() {
        return new ServiceRegistration(EchoWithModifyService.class.getSimpleName(), "1.0");
    }

    @Override
    public String callService(String request) {
        return "modified_" + request;
    }

}
