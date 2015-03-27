package eu.anmore.hubs.service;

import java.util.Collection;

public class ServiceRequest {

    private String serviceName;

    private ServiceVersion serviceVersion;

    private Collection<Tag> tags;

    private JsonObject data;

    /**
     * @deprecated Deserialization only
     */
    @Deprecated
    public ServiceRequest() {
    }

    public ServiceRequest(String serviceName, ServiceVersion serviceVersion, Collection<Tag> tags, JsonObject jsonIn) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.tags = tags;
        this.data = jsonIn;
    }

    public static ServiceRequest of(String serviceName, ServiceVersion serviceVersion, Collection<Tag> tags, JsonObject jsonIn) {
        return new ServiceRequest(serviceName, serviceVersion, tags, jsonIn);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceRequest{");
        sb.append("serviceName='").append(serviceName).append('\'');
        sb.append(", serviceVersion=").append(serviceVersion);
        sb.append(", tags=").append(tags);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    public JsonObject getData() {
        return data;
    }

    public String getServiceName() {
        return serviceName;
    }

}
