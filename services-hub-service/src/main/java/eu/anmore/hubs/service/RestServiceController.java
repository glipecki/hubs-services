package eu.anmore.hubs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

@RequestMapping("/v1/service")
@ResponseBody
public class RestServiceController implements ServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(RestServiceController.class);

    private ServiceSelector serviceSelector;

    private ServiceExecutor serviceExecutor;

    public RestServiceController(ServiceSelector serviceSelector, ServiceExecutor serviceExecutor) {
        this.serviceSelector = serviceSelector;
        this.serviceExecutor = serviceExecutor;
    }

    @RequestMapping(value = "/{serviceName}", method = RequestMethod.POST)
    @Override
    public String callService(@RequestBody String in,
                              @PathVariable String serviceName,
                              @RequestParam(required = false, defaultValue = "*") String serviceVersion,
                              @RequestParam(required = false) Collection<String> tags) {
        ServiceCall serviceCall = null;
        try {
            LOG.trace("> Calling service [name={}, version={}, tags={}, in={}]", serviceName, serviceVersion, tags, in);

            serviceCall = ServiceCall.ofServiceRequest(ServiceRequest.of(serviceName, ServiceVersion.of(serviceVersion), Tag.of(tags), JsonObject.of(in)), new Date());
            final ServiceEndpoint endpoint = serviceSelector.getEndpoint(serviceCall);
            final ServiceResponse serviceResponse = serviceExecutor.call(endpoint, serviceCall);

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

}
