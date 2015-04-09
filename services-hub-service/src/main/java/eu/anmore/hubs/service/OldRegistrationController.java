package eu.anmore.hubs.service;

import eu.anmore.hubs.HubInfo;
import eu.anmore.hubs.HubUuid;
import eu.anmore.hubs.registration.AddRegistrationResult;
import eu.anmore.hubs.registration.Registration;
import eu.anmore.hubs.service.tracker.db.DbServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/registration")
public class OldRegistrationController {

    private static final Logger LOG = LoggerFactory.getLogger(OldRegistrationController.class);
    private final DbServiceTracker serviceTracker;
    private HubUuid hubUuid;

    @Autowired
    public OldRegistrationController(DbServiceTracker serviceTracker, HubUuid hubUuid) {
        this.serviceTracker = serviceTracker;
        this.hubUuid = hubUuid;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public AddRegistrationResult registerServices(@RequestBody final Registration registration) {
        LOG.info("Registering endpoint [registration={}]", registration);
        serviceTracker.register(registration);
        return new AddRegistrationResult(new HubInfo(hubUuid));
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public String unregisterServices(@RequestBody final Registration registration) {
        LOG.info("Removing endpoint [registration={}]", registration);
        serviceTracker.unregister(registration.url);
        return "ok";
    }

}
