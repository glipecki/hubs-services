package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.api.spring.JsonServiceAdapter;
import eu.anmore.hubs.registration.ServiceRegistration;
import org.springframework.stereotype.Component;

//  curl -X POST http://localhost:8080/api/v1/services/EchoWithModifyService/call -H "Content-Type: application/json" -H "Accept: application/json" -d '"test"'
// "modified_test"
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
