package eu.anmore.hubs.service;

public class ServiceEndpoint {

    private final String name;

    private final String version;

    private final HubEndpoint hubEndpoint;

    public ServiceEndpoint(String name, String version, HubEndpoint hubEndpoint) {
        this.name = name;
        this.version = version;
        this.hubEndpoint = hubEndpoint;
    }

    public static ServiceEndpoint of(ServiceEntity serviceEntity) {
        return new ServiceEndpoint(serviceEntity.name, serviceEntity.version, HubEndpoint.of(serviceEntity.url));
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceEndpoint{");
        sb.append("name='").append(name).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", hubEndpoint=").append(hubEndpoint);
        sb.append('}');
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public HubEndpoint getHubEndpoint() {
        return hubEndpoint;
    }

}
