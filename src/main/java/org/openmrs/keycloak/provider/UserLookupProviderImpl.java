package org.openmrs.keycloak.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.storage.user.UserLookupProvider;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.data.UserDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserLookupProviderImpl implements UserLookupProvider {

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;
    private UserDAO userDAO;

    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers = new HashMap<String, UserModel>();

    public UserLookupProviderImpl(KeycloakSession session, Properties properties, ComponentModel model, Map<String, UserModel> loadedUsers) {
        this.session = session;
        this.properties = properties;
        this.model = model;
        this.loadedUsers = loadedUsers;
    }

    public UserModel getUserById(String id, RealmModel realmModel) {
        new UserAdapter(session, realmModel, model, userDAO.getUserByUsername(id));
    }

    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDAO.getUserByUserId(username));
    }

    public UserModel getUserByEmail(String email, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDAO.getUserByEmail(email));
    }
}
