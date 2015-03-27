package eu.anmore.hubs.service;

public interface ServiceSelector {

    ServiceEndpoint getEndpoint(ServiceCall serviceCall);

}
