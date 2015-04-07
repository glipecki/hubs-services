package eu.anmore.hubs.api.spring;

import eu.anmore.hubs.HubInfo;
import eu.anmore.hubs.registration.AddRegistrationResult;
import eu.anmore.hubs.registration.Registration;
import eu.anmore.hubs.service.HubService;
import eu.anmore.hubs.service.ServiceRequest;
import eu.anmore.hubs.service.ServiceResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
public class ServiceRegisterer {

    private final Collection<HubService> hubServices;
    private final String serviceUrl;
    private final List<String> hubUrls;
    private final ConcurrentHashMap<String, HubInfo> hubs = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Date> pings = new ConcurrentHashMap<>();

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

    @Scheduled(fixedRateString = "10000")
    public void checkHubCommunication() {
        hubs.entrySet()
                .stream()
                .forEach((e) -> {
                    final Date lastCheck = pings.get(e.getValue().getHubUuid().asString());
                    if (lastCheck == null || new Date().getTime() - lastCheck.getTime() > 30000) {
                        System.out.println("Hub is dead? - " + e.getKey());
                        pings.remove(e.getValue().getHubUuid().asString());
                        try {
                            registerServicesInHub();
                            System.out.println("Reconnected!");
                        } catch (Exception exception) {
                            System.out.println("Can't reconnect: " + exception.getMessage());
                        }
                    }
                });
    }

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    public String ping(@RequestBody final String body) {
        pings.put(body, new Date());
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
            try {
                AddRegistrationResult response = restTemplate
                        .postForObject(hubUrl + "/api/v1/registration", registration, AddRegistrationResult.class);
                hubs.put(hubUrl, response.getHubInfo());
                pings.put(response.getHubInfo().getHubUuid().asString(), new Date());
            } catch (HttpServerErrorException e) {
                System.out.println("FUCKUP: " + e.getResponseBodyAsString());
            }
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
