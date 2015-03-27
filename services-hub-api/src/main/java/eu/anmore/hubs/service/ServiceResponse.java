package eu.anmore.hubs.service;

public class ServiceResponse {

    // cachable = true/false
    // cacheTtl = x [ms]

    public String data;

    /**
     * @deprecated Deserialization only
     */
    @Deprecated
    public ServiceResponse() {
    }

    public ServiceResponse(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceResponse{");
        sb.append("data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
