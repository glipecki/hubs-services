package eu.anmore.hubs.registration;

public class ServiceRegistration {

    public String name;
    public String version;
    public String description;
    public String descriptor;

    /**
     * @deprecated Deserialization only
     */
    ServiceRegistration() {
    }

    public ServiceRegistration(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public ServiceRegistration(String name, String version, String description, String descriptor) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.descriptor = descriptor;
    }

}
