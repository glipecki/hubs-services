package eu.anmore.hubs.monitor.heartbeat;

import eu.anmore.hubs.event.Event;
import eu.anmore.hubs.service.HubEndpoint;

public class HubEndpointPositiveHeartbeatEvent implements Event {

    private HubEndpoint hubEndpoint;

    public HubEndpointPositiveHeartbeatEvent(HubEndpoint hubEndpoint) {
        this.hubEndpoint = hubEndpoint;
    }

}
