package eu.anmore.hubs.service;

import eu.anmore.hubs.registration.ServiceRegistration;
import eu.anmore.hubs.service.ServiceRequest;
import eu.anmore.hubs.service.ServiceResponse;

public interface HubService {

    ServiceRegistration getServiceRegistration();

    ServiceResponse call(ServiceRequest in);

}
