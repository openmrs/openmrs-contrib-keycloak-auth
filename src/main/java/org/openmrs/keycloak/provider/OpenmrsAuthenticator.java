package org.openmrs.keycloak.provider;


import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.user.UserLookupProvider;
import org.openmrs.keycloak.Security;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.data.UserDAO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class OpenmrsAuthenticator implements UserLookupProvider, CredentialInputValidator {

    @PersistenceContext
    private EntityManager em;

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;
    private UserDAO userDAO;

    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers = new HashMap<String, UserModel>();

    public OpenmrsAuthenticator(KeycloakSession session, Properties properties, ComponentModel model, Map<String, UserModel> loadedUsers) {
        this.session = session;
        this.properties = properties;
        this.model = model;
        this.loadedUsers = loadedUsers;
    }

    @Override
    public UserModel getUserById(String id, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDAO.getUserByUserId(Integer.parseInt(id)));
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDAO.getUserByUsername(username));
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDAO.getUserByEmail(email));
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD) && getPassword(userModel) != null;
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()))
            return false;

        String passwordOnRecord = userDAO.getUserPasswordOnRecord(userModel);

        String saltOnRecord = userDAO.getUserSaltOnRecord(userModel);

        String currentPassword = getPassword(userModel);

        if (passwordOnRecord == null || saltOnRecord == null || currentPassword == null) {
            return false;
        }

        return Security.hashMatches(passwordOnRecord, currentPassword + saltOnRecord);
    }

    public String getPassword(UserModel user) {
        String password = null;
        if (user instanceof UserAdapter) {
            password = ((UserAdapter) user).getPassword();
        }
        return password;
    }
}
