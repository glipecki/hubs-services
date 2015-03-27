package eu.anmore.hubs.registration;

import eu.anmore.hubs.service.ServiceRequest;
import eu.anmore.hubs.service.ServiceResponse;

public interface HubService {

    ServiceRegistration getServiceRegistration();

    ServiceResponse call(ServiceRequest in);

}
