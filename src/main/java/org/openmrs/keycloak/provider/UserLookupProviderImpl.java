package org.openmrs.keycloak.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserLookupProviderImpl implements UserLookupProvider {

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;

    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers=new HashMap<String, UserModel>();

    public UserLookupProviderImpl(KeycloakSession session, Properties properties, ComponentModel model, Map<String, UserModel> loadedUsers) {
        this.session = session;
        this.properties = properties;
        this.model = model;
        this.loadedUsers = loadedUsers;
    }

    public UserModel getUserById(String id, RealmModel realmModel) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(username, realmModel);
    }

    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            String password = properties.getProperty(username);
            if (password != null) {
                adapter = createAdapter(realmModel, username);
                loadedUsers.put(username, adapter);
            }
        }
        return adapter;
    }

    protected UserModel createAdapter(RealmModel realm, final String username) {
        return new AbstractUserAdapter(session, realm, model) {
            public String getUsername() {
                return username;
            }
        };
    }

    public UserModel getUserByEmail(String s, RealmModel realmModel) {
        return null;
    }
}
