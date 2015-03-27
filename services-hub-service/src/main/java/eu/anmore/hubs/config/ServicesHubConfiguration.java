package eu.anmore.hubs.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import eu.anmore.hubs.event.EventListener;
import eu.anmore.hubs.event.ServiceCalledSuccessfullyEvent;
import eu.anmore.hubs.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

@SpringBootApplication
public class ServicesHubConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceController serviceController(ServiceSelector serviceSelector, ServiceExecutor serviceExecutor) {
        return new RestServiceController(serviceSelector, serviceExecutor);
    }

    @Bean
    public ServiceSelector serviceSelector(ServiceRepository serviceRepository, EventBus eventBus) {
        return new SimpleServiceSelector(serviceRepository, eventBus);
    }

    @Bean
    public ServiceExecutor serviceExecutor(RestTemplate restTemplate, EventBus eventBus) {
        return new RemoteServiceExecutor(restTemplate, eventBus);
    }

    @Bean
    public Executor executor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolTaskExecutor;
    }

    @Bean
    public EventListener logExecutionTimeEventListener(GaugeService gaugeService, CounterService counterService) {
        return new EventListener() {

            private final Logger LOG = LoggerFactory.getLogger("ServiceExecutionTime");

            @Subscribe
            public void handleServiceCalledSuccessfullyEvent(ServiceCalledSuccessfullyEvent event) {
                final ServiceCall serviceCall = event.getServiceCall();
                String serviceName = serviceCall.getServiceRequest().getServiceName();
                counterService.increment("counter.services.call." + serviceName);
                gaugeService.submit("time.services.call." + serviceName, serviceCall.getProcessingTime());
                LOG.debug("{serviceName={},executionTime={}ms}", serviceName, serviceCall.getProcessingTime());
            }

        };
    }

    @Bean
    public EventBus eventBus(Executor executor) {
        return new AsyncEventBus(executor);
    }

    @Configuration
    public static class EventBusConfiguration {

        @Autowired(required = false)
        private List<EventListener> eventListeners;

        @Autowired
        private EventBus eventBus;

        @PostConstruct
        public void initializeEventListeners() {
            Optional
                    .ofNullable(eventListeners)
                    .orElse(Collections.emptyList())
                    .forEach(listener -> eventBus.register(listener));
        }

        @PreDestroy
        public void removeEventListeners() {
            Optional
                    .ofNullable(eventListeners)
                    .orElse(Collections.emptyList())
                    .forEach(listener -> eventBus.unregister(listener));
        }

    }

}
