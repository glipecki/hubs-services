package eu.anmore.hubs.api.spring;

import eu.anmore.hubs.registration.HubService;
import eu.anmore.hubs.registration.Registration;
import eu.anmore.hubs.service.ServiceRequest;
import eu.anmore.hubs.service.ServiceResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ServiceRegisterer {

    private final Collection<HubService> hubServices;

    private final String serviceUrl;

    private final List<String> hubUrls;

    public ServiceRegisterer(Collection<HubService> hubServices, String serviceUrl, List<String> hubUrls) {
        this.hubServices = hubServices;
        this.serviceUrl = serviceUrl;
        this.hubUrls = hubUrls;
    }

    @RequestMapping(value = "/{serviceName}/call", method = RequestMethod.POST)
    public ServiceResponse call(@PathVariable final String serviceName, @RequestBody final ServiceRequest in) {
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
        registration.url = this.serviceUrl;

        registration.services = hubServices.stream().map(HubService::getServiceRegistration).collect(Collectors
                .toList());

        for (String hubUrl : this.hubUrls) {
            String response = restTemplate
                    .postForObject(hubUrl + "/api/v1/registration", registration, String.class);
        }
    }

    @PreDestroy
    public void deregisterServicesFromHub() {
        RestTemplate restTemplate = new RestTemplate();

        Registration registration = new Registration();
        registration.url = this.serviceUrl;

        for (String hubUrl : this.hubUrls) {
            restTemplate.exchange(hubUrl + "/api/v1/registration", HttpMethod.DELETE, new HttpEntity(registration), String.class);
        }
    }

}
