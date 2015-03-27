package eu.anmore.hubs.service;

import com.google.common.eventbus.EventBus;
import eu.anmore.hubs.event.ServiceEndpointNotFoundEvent;

import java.util.Collection;
import java.util.stream.Collectors;

public class SimpleServiceSelector implements ServiceSelector {

    private ServiceRepository serviceRepository;

    private EventBus eventBus;

    public SimpleServiceSelector(ServiceRepository serviceRepository, EventBus eventBus) {
        this.serviceRepository = serviceRepository;
        this.eventBus = eventBus;
    }

    @Override
    public ServiceEndpoint getEndpoint(ServiceCall serviceCall) {
        final Collection<ServiceEntity> services = serviceRepository.findByName(serviceCall.getServiceRequest().getServiceName());
        // todo: change to serviceTracker.find(serviceName, ServiceMatcher). how to configure specific matcher from extensions?
        final ServiceEndpoint selectedEndpoint = nonEmptyList(services) ? pickServiceEndpoint(serviceCall, services) : null;
        if (selectedEndpoint != null) {
            serviceCall.setSelectedEndpoint(selectedEndpoint, services.stream().map(e -> ServiceEndpoint.of(e)).collect(Collectors.toList()));
            return selectedEndpoint;
        } else {
            return endpointNotFound(serviceCall);
        }
    }

    protected ServiceEndpoint endpointNotFound(ServiceCall serviceCall) {
        eventBus.post(ServiceEndpointNotFoundEvent.of(serviceCall));
        // todo: throw specific exception
        throw new RuntimeException("Service endpoint not found");
    }

    protected ServiceEndpoint pickServiceEndpoint(ServiceCall serviceCall, Collection<ServiceEntity> services) {
        return ServiceEndpoint.of(services.iterator().next());
    }

    private boolean nonEmptyList(Collection<ServiceEntity> services) {
        return services != null && !services.isEmpty();
    }

}
