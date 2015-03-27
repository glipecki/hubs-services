package eu.anmore.hubs.service;

import com.google.common.eventbus.EventBus;
import eu.anmore.hubs.event.ServiceCalledSuccessfullyEvent;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class RemoteServiceExecutor implements ServiceExecutor {

    private RestTemplate restTemplate;

    private EventBus eventBus;

    public RemoteServiceExecutor(RestTemplate restTemplate, EventBus eventBus) {
        this.restTemplate = restTemplate;
        this.eventBus = eventBus;
    }

    @Override
    public ServiceResponse call(ServiceEndpoint endpoint, ServiceCall serviceCall) {
        final ServiceResponse serviceResponse = getRemoteServiceResponse(endpoint, serviceCall);

        serviceCall.setServiceResponse(serviceResponse, new Date());
        eventBus.post(ServiceCalledSuccessfullyEvent.of(serviceCall));

        return serviceResponse;
    }

    protected ServiceResponse getRemoteServiceResponse(ServiceEndpoint endpoint, ServiceCall serviceCall) {
        final String serviceUrl = String.format("%s/%s/call", endpoint.getHubEndpoint().getUrl(), endpoint.getName());
        serviceCall.setRequestUrl(serviceUrl);
        try {
            return restTemplate.postForObject(serviceUrl, serviceCall.getServiceRequest(), ServiceResponse.class);
        } catch (HttpClientErrorException e) {
            serviceCall.setExceptionMessageDetails(e.getResponseBodyAsString());
            throw e;
        }
    }

}
