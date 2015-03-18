package eu.anmore.hubs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @RequestMapping(value = "/{serviceName}/call", method = RequestMethod.POST)
    public String call(@PathVariable final String serviceName, @RequestBody final String jsonIn) {
        Collection<ServiceEntity> serviceInstances = serviceRepository.findByName(serviceName);
        if (serviceInstances.isEmpty()) {
            throw new RuntimeException(String.format("Service not found [serviceName=%s]", serviceName));
        } else {
            ServiceEntity serviceInstance = serviceInstances.iterator().next();
            return new RestTemplate().postForObject(String.format("%s/%s/call", serviceInstance.url, serviceName),
                    jsonIn,
                    String.class);
        }
    }

}
