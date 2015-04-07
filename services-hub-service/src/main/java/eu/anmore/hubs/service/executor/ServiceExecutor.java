package eu.anmore.hubs.service.executor;

import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;
import eu.anmore.hubs.service.ServiceResponse;

public interface ServiceExecutor {

    ServiceResponse call(ServiceEndpoint endpoint, ServiceCall serviceCall);

}
