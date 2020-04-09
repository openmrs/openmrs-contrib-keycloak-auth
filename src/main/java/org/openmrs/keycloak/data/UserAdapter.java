package org.openmrs.keycloak.data;

import lombok.Getter;
import lombok.Setter;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage;

public class UserAdapter extends AbstractUserAdapterFederatedStorage {

    private UserModel userModel;
    private String keycloakId;

    public UserAdapter(KeycloakSession session, RealmModel realm, ComponentModel storageProviderModel, UserModel userModel) {
        super(session, realm, storageProviderModel);
        this.userModel = userModel;
        keycloakId = StorageId.keycloakId(storageProviderModel, String.valueOf(userModel.getUserId()));
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    @Override
    public void setUsername(String username) {
        userModel.setUsername(username);
    }

    @Override
    public void setEmail(String email) {
        userModel.setEmail(email);
    }

    @Override
    public String getEmail() {
        return userModel.getEmail();
    }
}
