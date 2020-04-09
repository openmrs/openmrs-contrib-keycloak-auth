package org.openmrs.keycloak.data;

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
        keycloakId = StorageId.keycloakId(storageProviderModel, userModel.getUserId());
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    @Override
    public void setUsername(String s) {
        userModel.setUsername(s);
    }

    @Override
    public void setEmail(String email) {
        userModel.setEmail(email);
    }

    @Override
    public String getEmail() {
        return userModel.getEmail();
    }

    @Override
    public String getId() {
        return keycloakId;
    }

}
