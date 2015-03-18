package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.registration.HubService;
import eu.anmore.hubs.registration.ServiceRegistration;
import org.springframework.stereotype.Component;

@Component
public class EchoService implements HubService {

    @Override
    public ServiceRegistration getServiceRegistration() {
        return new ServiceRegistration(EchoService.class.getSimpleName(), "1.0");
    }

    @Override
    public String call(String in) {
        return in;
    }

}
