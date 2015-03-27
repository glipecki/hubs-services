package eu.anmore.hubs.event;

import eu.anmore.hubs.service.ServiceCall;

public class ServiceCalledSuccessfullyEvent implements Event {

    private ServiceCall serviceCall;

    public ServiceCalledSuccessfullyEvent(ServiceCall serviceCall) {
        this.serviceCall = serviceCall;
    }

    public ServiceCall getServiceCall() {
        return serviceCall;
    }

    public static Object of(ServiceCall serviceCall) {
        return new ServiceCalledSuccessfullyEvent(serviceCall);
    }

}
