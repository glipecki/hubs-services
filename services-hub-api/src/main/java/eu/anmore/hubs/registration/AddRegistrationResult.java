package eu.anmore.hubs.registration;

import eu.anmore.hubs.HubInfo;

public class AddRegistrationResult {

    private HubInfo hubInfo;

    AddRegistrationResult() {
    }

    public AddRegistrationResult(HubInfo hubInfo) {
        this.hubInfo = hubInfo;
    }

    public HubInfo getHubInfo() {
        return hubInfo;
    }

}
