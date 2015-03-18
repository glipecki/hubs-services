package eu.anmore.hubs.registration;

public interface HubService {

    ServiceRegistration getServiceRegistration();

    String call(String in);

}
