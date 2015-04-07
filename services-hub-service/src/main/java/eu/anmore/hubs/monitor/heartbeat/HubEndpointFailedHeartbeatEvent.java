package eu.anmore.hubs.monitor.heartbeat;

import eu.anmore.hubs.event.Event;
import eu.anmore.hubs.service.HubEndpoint;

public class HubEndpointFailedHeartbeatEvent implements Event {

    private HubEndpoint hubEndpoint;
    private Exception exception;

    public HubEndpointFailedHeartbeatEvent(HubEndpoint hubEndpoint, Exception exception) {
        this.hubEndpoint = hubEndpoint;
        this.exception = exception;
    }

}
