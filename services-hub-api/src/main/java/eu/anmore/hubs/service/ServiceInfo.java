package eu.anmore.hubs.service;

public class ServiceInfo {

    private String serviceName;

    public ServiceInfo(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceInfo{");
        sb.append("serviceName='").append(serviceName).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
