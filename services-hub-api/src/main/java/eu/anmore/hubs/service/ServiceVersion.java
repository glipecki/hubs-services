package eu.anmore.hubs.service;

public class ServiceVersion {

    private String rawServiceVersion;

    /**
     * @deprecated Deserialization only
     */
    @Deprecated
    public ServiceVersion() {
    }

    public ServiceVersion(String rawServiceVersion) {
        this.rawServiceVersion = rawServiceVersion;
    }

    public static ServiceVersion of(String serviceVersion) {
        return new ServiceVersion(serviceVersion);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceVersion{");
        sb.append("rawServiceVersion='").append(rawServiceVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
