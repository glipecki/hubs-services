package eu.anmore.hubs.config;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import eu.anmore.hubs.HubUuid;
import eu.anmore.hubs.event.EventListener;
import eu.anmore.hubs.event.ServiceCalledSuccessfullyEvent;
import eu.anmore.hubs.monitor.heartbeat.HeartbeatHealthMonitor;
import eu.anmore.hubs.service.RestServiceController;
import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceController;
import eu.anmore.hubs.service.executor.RemoteServiceExecutor;
import eu.anmore.hubs.service.executor.ServiceExecutor;
import eu.anmore.hubs.service.selector.ServiceSelector;
import eu.anmore.hubs.service.selector.SimpleServiceSelector;
import eu.anmore.hubs.service.tracker.ServiceTracker;
import eu.anmore.hubs.service.tracker.db.DbServiceTracker;
import eu.anmore.hubs.service.tracker.db.ServiceRepository;
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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableScheduling
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
    public HubUuid hubUuid() {
        return new HubUuid(UUID.randomUUID());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceController serviceController(ServiceSelector serviceSelector, ServiceExecutor serviceExecutor, EventBus eventBus) {
        return new RestServiceController(serviceSelector, serviceExecutor, eventBus);
    }

    @Bean
    public ServiceSelector serviceSelector(ServiceRepository serviceRepository, ServiceTracker serviceTracker) {
        return new SimpleServiceSelector(serviceRepository, serviceTracker);
    }

    @Bean
    public ServiceExecutor serviceExecutor(RestTemplate restTemplate) {
        return new RemoteServiceExecutor(restTemplate);
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
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler threadPoolScheduler = new ThreadPoolTaskScheduler();
        threadPoolScheduler.setPoolSize(2);
        threadPoolScheduler.setWaitForTasksToCompleteOnShutdown(true);
        return threadPoolScheduler;
    }

    @Bean
    public HeartbeatHealthMonitor heartbeatHealthMonitor(DbServiceTracker serviceTracker, RestTemplate restTemplate, HubUuid hubUuid, EventBus eventBus) {
        return new HeartbeatHealthMonitor(serviceTracker, restTemplate, hubUuid, eventBus);
    }

    @Bean
    public EventListener logExecutionTimeEventListener(GaugeService gaugeService, CounterService counterService) {
        return new EventListener() {

            private final Logger LOG = LoggerFactory.getLogger("ServiceExecutionTime");

            @Subscribe
            public void handleServiceCalledSuccessfullyEvent(ServiceCalledSuccessfullyEvent event) {
                final ServiceCall serviceCall = event.getServiceCall();
                // todo: put more stats, based on service name, endpoint and version
                // String serviceDescription = String.format("%s.%s.%s",
                // serviceCall.getEndpoint().getName(),
                // serviceCall.getEndpoint().getHubEndpoint(),
                // serviceCall.getEndpoint().getVersion());
                String serviceDescription = String.format("%s",
                        serviceCall.getEndpoint().getName());
                counterService.increment("counter.services.call." + serviceDescription);
                gaugeService.submit("time.services.call." + serviceDescription, serviceCall.getProcessingTime());
                LOG.debug("{serviceDescription={},executionTime={}ms}", serviceDescription, serviceCall.getProcessingTime());
            }

        };
    }

    @Bean
    public EventBus eventBus(Executor executor) {
        return new AsyncEventBus(executor);
    }

    @Configuration
    public static class EventBusListenersConfiguration {

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
