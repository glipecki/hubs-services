package eu.anmore.hubs.service;

public interface ServiceExecutor {

    ServiceResponse call(ServiceEndpoint endpoint, ServiceCall serviceCall);

}
