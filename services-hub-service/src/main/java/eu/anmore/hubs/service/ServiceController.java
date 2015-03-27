package eu.anmore.hubs.service;

import java.util.Collection;

public interface ServiceController {

    String callService(final String in, final String serviceName, final String serviceVersion, final Collection<String> tags);

}
