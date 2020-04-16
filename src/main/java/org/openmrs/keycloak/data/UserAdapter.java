package org.openmrs.keycloak.data;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;
import org.openmrs.keycloak.models.OpenmrsUserModel;

public class UserAdapter extends AbstractUserAdapterFederatedStorage {

    private final OpenmrsUserModel openmrsUserModel;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, OpenmrsUserModel openmrsUserModel) {
        super(session, realm, storageProviderModel);
        this.openmrsUserModel = openmrsUserModel;
    }

    @Override
    public String getId() {
        return String.valueOf(openmrsUserModel.getUserId());
    }

    @Override
    public String getUsername() {
        return openmrsUserModel.getUsername();
    }

    @Override
    public void setUsername(String username) {
        openmrsUserModel.setUsername(username);
    }

    @Override
    public String getEmail() {
        return openmrsUserModel.getEmail();
    }

    @Override
    public void setEmail(String email) {
        openmrsUserModel.setEmail(email);
    }
}
