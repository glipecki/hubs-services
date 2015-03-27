package eu.anmore.hubs.event;

import eu.anmore.hubs.service.ServiceCall;

public class ServiceEndpointNotFoundEvent implements Event {

    private ServiceCall serviceCall;

    public ServiceEndpointNotFoundEvent(ServiceCall serviceCall) {
        this.serviceCall = serviceCall;
    }

    public static ServiceEndpointNotFoundEvent of(ServiceCall serviceCall) {
        return new ServiceEndpointNotFoundEvent(serviceCall);
    }

}
