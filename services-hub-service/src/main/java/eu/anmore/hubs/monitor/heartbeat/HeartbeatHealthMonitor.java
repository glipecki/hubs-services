package eu.anmore.hubs.monitor.heartbeat;

import com.google.common.eventbus.EventBus;
import eu.anmore.hubs.HubUuid;
import eu.anmore.hubs.service.HubEndpoint;
import eu.anmore.hubs.service.tracker.db.DbServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class HeartbeatHealthMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatHealthMonitor.class);
    private final DbServiceTracker serviceTracker;
    private final EventBus eventBus;
    private HubUuid hubUuid;
    private RestTemplate restTemplate;

    public HeartbeatHealthMonitor(DbServiceTracker serviceTracker, RestTemplate restTemplate, HubUuid hubUuid, EventBus eventBus) {
        this.serviceTracker = serviceTracker;
        this.restTemplate = restTemplate;
        this.hubUuid = hubUuid;
        this.eventBus = eventBus;
    }

    @Scheduled(fixedRateString = "${app.health.heartbeat.fixedRate}")
    public void monitor() {
        serviceTracker
                .getEndpoints()
                .orElse(Collections.emptyList())
                .stream()
                .forEach(e -> checkEndpointHealth(e));
    }

    private void checkEndpointHealth(HubEndpoint hubEndpoint) {
        try {
            String result = restTemplate.postForObject(hubEndpoint.getUrl() + "/ping", hubUuid.asString(), String.class);
            eventBus.post(new HubEndpointPositiveHeartbeatEvent(hubEndpoint));
        } catch (Exception e) {
            eventBus.post(new HubEndpointFailedHeartbeatEvent(hubEndpoint, e));
        }
    }

}
