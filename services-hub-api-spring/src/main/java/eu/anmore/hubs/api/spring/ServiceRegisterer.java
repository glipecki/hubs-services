package eu.anmore.hubs.api.spring;

import eu.anmore.hubs.registration.HubService;
import eu.anmore.hubs.registration.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class ServiceRegisterer {

    private final Collection<HubService> hubServices;

    private final String url;

    private final String hubUrl;

    @Autowired
    public ServiceRegisterer(Collection<HubService> hubServices) {
        this.hubServices = hubServices;
        this.url = "http://localhost:8081/";
        this.hubUrl = "http://localhost:8080";
    }

    @RequestMapping(value = "/{serviceName}/call", method = RequestMethod.POST)
    public String call(@PathVariable final String serviceName, @RequestBody final String in) {
        for (HubService hubService : hubServices) {
            if (hubService.getServiceRegistration().name.equals(serviceName)) {
                return hubService.call(in);
            }
        }
        throw new RuntimeException(String.format("Service not found [serviceName=%s]", serviceName));
    }

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public String ping() {
        return "pong";
    }

    @PostConstruct
    public void registerServicesInHub() {
        RestTemplate restTemplate = new RestTemplate();

        Registration registration = new Registration();
        registration.url = this.url;

        registration.services = hubServices.stream().map(HubService::getServiceRegistration).collect(Collectors
                .toList());

        String response = restTemplate
                .postForObject(this.hubUrl + "/api/v1/registration/register", registration, String.class);
    }

    @PreDestroy
    public void deregisterServicesFromHub() {
        RestTemplate restTemplate = new RestTemplate();

        Registration registration = new Registration();
        registration.url = this.url;

        restTemplate.postForObject(this.hubUrl + "/api/v1/registration/unregister", registration, String.class);
    }

}
