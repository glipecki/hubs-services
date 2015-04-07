package eu.anmore.hubs;

public class HubInfo {

    private HubUuid hubUuid;

    HubInfo() {
    }

    public HubInfo(HubUuid hubUuid) {
        this.hubUuid = hubUuid;
    }

    public HubUuid getHubUuid() {
        return hubUuid;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HubInfo{");
        sb.append("hubUuid=").append(hubUuid);
        sb.append('}');
        return sb.toString();
    }
    
}
