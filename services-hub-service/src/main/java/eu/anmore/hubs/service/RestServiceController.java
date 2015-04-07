package eu.anmore.hubs.service;

import com.google.common.eventbus.EventBus;
import eu.anmore.hubs.event.ServiceCalledSuccessfullyEvent;
import eu.anmore.hubs.event.ServiceEndpointNotFoundEvent;
import eu.anmore.hubs.service.executor.ServiceExecutor;
import eu.anmore.hubs.service.selector.ServiceSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

@RequestMapping("/v1/service")
@ResponseBody
public class RestServiceController implements ServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(RestServiceController.class);

    private ServiceSelector serviceSelector;

    private ServiceExecutor serviceExecutor;

    private EventBus eventBus;

    public RestServiceController(ServiceSelector serviceSelector, ServiceExecutor serviceExecutor, EventBus eventBus) {
        this.serviceSelector = serviceSelector;
        this.serviceExecutor = serviceExecutor;
        this.eventBus = eventBus;
    }

    @RequestMapping(value = "/{serviceName}", method = RequestMethod.POST)
    @Override
    public String callService(@RequestBody final String in,
                              @PathVariable final String serviceName,
                              @RequestParam(required = false, defaultValue = "*") final String serviceVersion,
                              @RequestParam(required = false) final Collection<String> tags) {
        final ServiceCall serviceCall = new ServiceCall();
        try {
            LOG.trace("> Calling service [name={}, version={}, tags={}, in={}]", serviceName, serviceVersion, tags, in);

            // fill ServiceCall with request
            serviceCall.setServiceRequest(ServiceRequest.of(serviceName, ServiceVersion.of(serviceVersion), Tag.of(tags), JsonObject.of(in)), new Date());

            // get service endpoint based on ServiceCall
            final Optional<ServiceEndpoint> endpoint = serviceSelector.getEndpoint(serviceCall);
            ifPresent(endpoint, e -> serviceCall.setSelectedEndpoint(e), endpointNotFound(serviceCall));

            // call service and store response in ServiceCall
            final ServiceResponse serviceResponse = serviceExecutor.call(endpoint.get(), serviceCall);
            serviceCall.setServiceResponse(serviceResponse, new Date());
            eventBus.post(ServiceCalledSuccessfullyEvent.of(serviceCall));

            LOG.trace("< service result [out={}]", serviceResponse.data);
            LOG.debug("Service called successfully [serviceCall={}]", serviceCall);
            return serviceResponse.data;
        } catch (Exception exception) {
            if (serviceCall != null) {
                serviceCall.setException(exception, new Date());
            }
            LOG.error("Exception while handling service call [serviceCall={}, exceptionMessage={}]", serviceCall, exception.getMessage());
            throw exception;
        }
    }

    private <T> void ifPresent(Optional<T> optional, Consumer<T> presentConsumer, Consumer<Optional<T>> notPresentConsumer) {
        if (optional.isPresent()) {
            presentConsumer.accept(optional.get());
        } else {
            notPresentConsumer.accept(optional);
        }
    }

    private <T> Consumer<Optional<T>> endpointNotFound(ServiceCall serviceCall) {
        return (o) -> {
            eventBus.post(ServiceEndpointNotFoundEvent.of(serviceCall));
            throw new RuntimeException("Service endpoint not found");
        };
    }


}
