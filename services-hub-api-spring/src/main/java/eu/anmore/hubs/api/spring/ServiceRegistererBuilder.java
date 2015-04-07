package eu.anmore.hubs.api.spring;

import eu.anmore.hubs.service.HubService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceRegistererBuilder {

    private final ApplicationContext applicationContext;

    private List<String> hubUrls = new ArrayList<>();

    private String serviceUrl;

    public static ServiceRegistererBuilder serviceRegistererBuilder(ApplicationContext applicationContext) {
        return new ServiceRegistererBuilder(applicationContext);
    }

    private ServiceRegistererBuilder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ServiceRegistererBuilder withServiceUrl(String serviceUrl){
        this.serviceUrl = serviceUrl;
        return this;
    }

    public ServiceRegistererBuilder withHubUrl(String hubUrl) {
        this.hubUrls.add(hubUrl);
        return this;
    }

    public ServiceRegisterer get() {
        Collection<HubService> hubServices = applicationContext.getBeansOfType(HubService.class).values();
        System.out.println("Services to register: " + hubServices);
        return new ServiceRegisterer(hubServices, serviceUrl, hubUrls);
    }

}
