package eu.anmore.hubs.service;

public class HubEndpoint {

    private String url;

    public HubEndpoint(String url) {
        this.url = url;
    }

    public static HubEndpoint of(String url) {
        return new HubEndpoint(url);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HubEndpoint{");
        sb.append("url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

}
