package eu.anmore.hubs.service.selector;

import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;
import eu.anmore.hubs.service.tracker.ServiceTracker;
import eu.anmore.hubs.service.tracker.db.ServiceRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class SimpleServiceSelector implements ServiceSelector {

    private ServiceRepository serviceRepository;

    private ServiceTracker serviceTracker;

    public SimpleServiceSelector(ServiceRepository serviceRepository, ServiceTracker serviceTracker) {
        this.serviceRepository = serviceRepository;
        this.serviceTracker = serviceTracker;
    }

    @Override
    public Optional<ServiceEndpoint> getEndpoint(ServiceCall serviceCall) {
        final Optional<Collection<ServiceEndpoint>> serviceEndpoints = serviceTracker.getEndpoints(serviceCall);
        serviceCall.setEndpointMatches(serviceEndpoints.orElseGet(Collections::emptyList));

        if (serviceEndpoints.isPresent() && nonEmptyList(serviceEndpoints.get())) {
            return Optional.ofNullable(pickServiceEndpoint(serviceCall, serviceEndpoints.get()));
        } else {
            return Optional.empty();
        }
    }

    protected ServiceEndpoint pickServiceEndpoint(ServiceCall serviceCall, Collection<ServiceEndpoint> services) {
        return services.iterator().next();
    }

    private boolean nonEmptyList(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

}
