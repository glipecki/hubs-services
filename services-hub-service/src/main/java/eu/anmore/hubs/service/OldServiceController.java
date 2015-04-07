package eu.anmore.hubs.service;

import eu.anmore.hubs.service.tracker.db.ServiceEntity;
import eu.anmore.hubs.service.tracker.db.ServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/services")
public class OldServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(OldServiceController.class);
    private final ServiceRepository serviceRepository;
    private final RestTemplate restTemplate;
    private Long sumTime = 0l;
    private Long counter = 0l;

    @Autowired
    public OldServiceController(ServiceRepository serviceRepository, RestTemplate restTemplate) {
        this.serviceRepository = serviceRepository;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/{serviceName}/call", method = RequestMethod.POST)
    public String call(@PathVariable final String serviceName, @RequestBody final String jsonIn, @RequestParam
            (defaultValue = "*") final String version) {
        Collection<ServiceEntity> serviceInstances = serviceRepository.findByName(serviceName);
        if (serviceInstances.isEmpty()) {
            throw new RuntimeException(String.format("Service not found [serviceName=%s]", serviceName));
        } else {
            ServiceEntity serviceInstance = serviceInstances.iterator().next();
            long startTime = System.currentTimeMillis();
            String result = restTemplate.postForObject(String.format("%s/%s/call", serviceInstance.url, serviceName),
                    jsonIn,
                    String.class);
            long endTime = System.currentTimeMillis();
            sumTime += (endTime - startTime);
            counter++;
            LOG.info("Request handling time: {}ms (avg: {}ms)", (endTime - startTime), (sumTime / counter));
            return result;
        }
    }

}
