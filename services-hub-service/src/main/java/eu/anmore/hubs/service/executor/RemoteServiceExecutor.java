package eu.anmore.hubs.service.executor;

import eu.anmore.hubs.service.ServiceCall;
import eu.anmore.hubs.service.ServiceEndpoint;
import eu.anmore.hubs.service.ServiceResponse;
import eu.anmore.hubs.service.executor.ServiceExecutor;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class RemoteServiceExecutor implements ServiceExecutor {

    private RestTemplate restTemplate;

    public RemoteServiceExecutor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ServiceResponse call(ServiceEndpoint endpoint, ServiceCall serviceCall) {
        return getRemoteServiceResponse(endpoint, serviceCall);
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
