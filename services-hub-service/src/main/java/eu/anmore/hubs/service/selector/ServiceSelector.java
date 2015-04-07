package eu.anmore.hubs.service.selector;

import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;

import java.util.Optional;

public interface ServiceSelector {

    Optional<ServiceEndpoint> getEndpoint(ServiceCall serviceCall);

}
