package eu.anmore.hubs.registration;

import java.util.ArrayList;
import java.util.List;

public class Registration {

    public String url;
    public List<ServiceRegistration> services = new ArrayList<>();

    public Registration() {
    }

    public Registration(String url, List<ServiceRegistration> services) {
        this.url = url;
        this.services.addAll(services);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Registration{");
        sb.append("url='").append(url).append('\'');
        sb.append(", services=").append(services);
        sb.append('}');
        return sb.toString();
    }

}
