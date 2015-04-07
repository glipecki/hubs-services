package eu.anmore.hubs.service.tracker;

import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;

import java.util.Collection;
import java.util.Optional;

public interface ServiceTracker {

    Optional<Collection<ServiceEndpoint>> getEndpoints(ServiceCall serviceCall);

}
