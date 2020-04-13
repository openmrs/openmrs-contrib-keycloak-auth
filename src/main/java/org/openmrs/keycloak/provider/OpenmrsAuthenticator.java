package org.openmrs.keycloak.provider;


import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.provider.Provider;
import org.keycloak.storage.user.UserLookupProvider;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.data.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class OpenmrsAuthenticator implements UserLookupProvider, CredentialInputValidator, Provider {

    @PersistenceContext
    private EntityManager em;

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;
    private UserDAO userDAO;

    private static final Logger log = LoggerFactory.getLogger(OpenmrsAuthenticator.class);


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
        return credentialType.equals(PasswordCredentialModel.PASSWORD);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        return credentialType.equals(PasswordCredentialModel.PASSWORD);
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!((credentialInput instanceof UserCredentialModel) && !supportsCredentialType(credentialInput.getType()))) {
            return false;
        }

        String[] passwordAndSalt = new String[2];
        passwordAndSalt = userDAO.getUserPasswordAndSaltOnRecord(userModel);

        String passwordOnRecord = passwordAndSalt[0];

        String saltOnRecord = passwordAndSalt[1];

        String currentPassword = credentialInput.getChallengeResponse();

        if (passwordOnRecord == null || saltOnRecord == null || currentPassword == null) {
            return false;
        }

        String passwordToHash = currentPassword + saltOnRecord;

        String algorithm = "SHA-512";
        MessageDigest md;
        byte[] input;
        try {
            md = MessageDigest.getInstance(algorithm);
            input = passwordToHash.getBytes(StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        log.error("No such Algorithm exist");

        return passwordOnRecord.equals(hexString(md.digest(input)));
    }

    private String hexString(byte[] block) {
        StringBuilder buf = new StringBuilder();
        char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int high;
        int low;
        for (byte aBlock : block) {
            high = ((aBlock & 0xf0) >> 4);
            low = (aBlock & 0x0f);
            buf.append(hexChars[high]);
            buf.append(hexChars[low]);
        }

        return buf.toString();
    }

    @Override
    public void close() {
    }
}
