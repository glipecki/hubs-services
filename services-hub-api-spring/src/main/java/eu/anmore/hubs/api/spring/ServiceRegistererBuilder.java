package eu.anmore.hubs.api.spring;

import eu.anmore.hubs.registration.HubService;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

public class ServiceRegistererBuilder {

    private final ApplicationContext applicationContext;

    public static ServiceRegistererBuilder serviceRegistererBuilder(ApplicationContext applicationContext) {
        return new ServiceRegistererBuilder(applicationContext);
    }

    private ServiceRegistererBuilder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ServiceRegisterer get() {
        Collection<HubService> hubServices = applicationContext.getBeansOfType(HubService.class).values();
        System.out.println("Services to register: " + hubServices);
        return new ServiceRegisterer(hubServices);
    }

}
