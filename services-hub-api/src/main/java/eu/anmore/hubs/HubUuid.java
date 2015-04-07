package eu.anmore.hubs;

import java.util.UUID;

public class HubUuid {

    private UUID uuid;

    HubUuid() {
    }

    public HubUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String asString() {
        return uuid.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HubUuid{");
        sb.append("uuid=").append(uuid);
        sb.append('}');
        return sb.toString();
    }

}
