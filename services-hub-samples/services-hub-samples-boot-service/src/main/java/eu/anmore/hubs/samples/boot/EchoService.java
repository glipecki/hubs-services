package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.registration.HubService;
import eu.anmore.hubs.registration.ServiceRegistration;
import eu.anmore.hubs.service.ServiceRequest;
import eu.anmore.hubs.service.ServiceResponse;
import org.springframework.stereotype.Component;

//  curl -X POST http://localhost:8080/api/v1/services/EchoService/call -H "Content-Type: application/json" -H "Accept: application/json" -d 'test'
// test
@Component
public class EchoService implements HubService {

    @Override
    public ServiceRegistration getServiceRegistration() {
        return new ServiceRegistration(EchoService.class.getSimpleName(), "1.0");
    }

    @Override
    public ServiceResponse call(ServiceRequest in) {
        return new ServiceResponse(in.getData().getRawJson());
    }

}
