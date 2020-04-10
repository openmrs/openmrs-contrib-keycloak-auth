package org.openmrs.keycloak.provider;


import org.hibernate.type.StandardBasicTypes;
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
import javax.persistence.TypedQuery;
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

    //TODO Not sure how to get the entered password
    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()))
            return false;
//TODO Should all the query part be shifted to UserDAO
        TypedQuery<org.openmrs.keycloak.models.UserModel> query = em.createQuery("select u from UserModel u where u.username = :username", org.openmrs.keycloak.models.UserModel.class);
        query.setParameter("username", userModel.getUsername());
        org.openmrs.keycloak.models.UserModel user = query.getSingleResult();

        TypedQuery<String> queryPassword = em.
                createQuery("select password from users where user_id = :userId", String.class);
        queryPassword.setParameter("password", StandardBasicTypes.STRING);
        queryPassword.setParameter("userId", user.getUserId());
        String passwordOnRecord = queryPassword.getSingleResult();

        TypedQuery<String> querySalt = em.
                createQuery("select salt from users where user_id = :userId", String.class);
        querySalt.setParameter("salt", StandardBasicTypes.STRING);
        querySalt.setParameter("userId", user.getUserId());
        String saltOnRecord = querySalt.getSingleResult();

        return passwordOnRecord != null && Security.hashMatches(passwordOnRecord, getPassword(userModel) + saltOnRecord);
    }

    public String getPassword(UserModel user) {
        String password = null;
        if (user instanceof UserAdapter) {
            password = ((UserAdapter) user).getPassword();
        }
        return password;
    }
}
