package eu.anmore.hubs.service.tracker.db;

import eu.anmore.hubs.registration.Registration;
import eu.anmore.hubs.service.HubEndpoint;
import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;
import eu.anmore.hubs.service.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DbServiceTracker implements ServiceTracker {

    private static final Logger LOG = LoggerFactory.getLogger(DbServiceTracker.class);

    private final ServiceRepository serviceRepository;

    @Autowired
    public DbServiceTracker(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public void register(Registration registration) {
        if (serviceRepository.countByUrl(registration.url) > 0) {
            LOG.debug("Registration already defined, overwriting [url={}]", registration.url);
            serviceRepository.deleteByUrl(registration.url);
        }
        serviceRepository.save(registration
                .services
                .stream()
                .map(s -> new ServiceEntity(registration.url, s.name, s.version, s.description))
                .collect(Collectors.toList()));

    }

    public void unregister(String url) {
        serviceRepository.deleteByUrl(url);
    }

    public Collection<String> getServices() {
        return serviceRepository
                .findAll()
                .parallelStream()
                .map(s -> s.name)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Collection<ServiceEndpoint>> getEndpoints(ServiceCall serviceCall) {
        // todo: change to serviceTracker.find(serviceName, ServiceMatcher). how to configure specific matcher from extensions?
        return Optional.ofNullable(
                serviceRepository
                        .findByName(serviceCall.getServiceRequest().getServiceName())
                        .stream()
                        .map(e -> e.toServiceEndpoint())
                        .collect(Collectors.toList()));
    }

    public Optional<Collection<HubEndpoint>> getEndpoints() {
        return Optional
                .ofNullable(serviceRepository
                        .findAll()
                        .stream()
                        .map(e -> e.url)
                        .distinct()
                        .map(url -> HubEndpoint.of(url))
                        .collect(Collectors.toList()));
    }

    // service registration 1-* service 1-* service version 1-* service instances

}
