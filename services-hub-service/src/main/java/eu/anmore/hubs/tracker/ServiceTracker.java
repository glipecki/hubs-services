package eu.anmore.hubs.tracker;

import eu.anmore.hubs.registration.Registration;
import eu.anmore.hubs.service.ServiceEntity;
import eu.anmore.hubs.service.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ServiceTracker {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTracker.class);

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceTracker(ServiceRepository serviceRepository) {
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
                .stream()
                .map(s -> s.name)
                .collect(Collectors.toList());
    }

    // service registration 1-* service 1-* service version 1-* service instances

}
