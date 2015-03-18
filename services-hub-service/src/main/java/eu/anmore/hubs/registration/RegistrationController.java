package eu.anmore.hubs.registration;

import eu.anmore.hubs.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/registration")
public class RegistrationController {

    private final ServiceTracker serviceTracker;

    @Autowired
    public RegistrationController(ServiceTracker serviceTracker) {
        this.serviceTracker = serviceTracker;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerServices(@RequestBody final Registration registration) {
        LOG.info("Registering remote services [registration={}]", registration);
        serviceTracker.register(registration);
        return "ok";
    }

    @RequestMapping(value = "/unregister", method = RequestMethod.POST)
    public String unregisterServices(@RequestBody final Registration registration) {
        LOG.info("Unregistering remote services [registration={}]", registration);
        serviceTracker.unregister(registration.url);
        return "ok";
    }

    @RequestMapping("/services")
    public Collection<String> services() {
        return serviceTracker.getServices();
    }

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationController.class);

}
