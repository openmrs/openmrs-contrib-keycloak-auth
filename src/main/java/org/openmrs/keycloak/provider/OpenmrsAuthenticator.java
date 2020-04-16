package org.openmrs.keycloak.provider;


import lombok.AccessLevel;
import lombok.Setter;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.data.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Setter(AccessLevel.PACKAGE)
public class OpenmrsAuthenticator implements UserLookupProvider, CredentialInputValidator, UserStorageProvider {

    protected KeycloakSession session;
    protected ComponentModel model;

//    @Setter(onMethod = @__({@Inject}))
    protected UserDao userDao=new UserDao();

    private static final Logger log = LoggerFactory.getLogger(OpenmrsAuthenticator.class);

    public OpenmrsAuthenticator(KeycloakSession session, ComponentModel model) {
        this.session = session;
        this.model = model;
    }

    @Override
    public UserModel getUserById(String id, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDao.getUserByUserId(Integer.parseInt(id)));
    }

    @Override
    public UserModel getUserByUsername(String username, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDao.getUserByUsername(username));
    }

    @Override
    public UserModel getUserByEmail(String email, RealmModel realmModel) {
        return new UserAdapter(session, realmModel, model, userDao.getUserByEmail(email));
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

        Object[] passwordAndSalt;
        try {
            passwordAndSalt = userDao.getUserPasswordAndSaltOnRecord(userModel);
        } catch (PersistenceException e) {
            log.error("Caught exception while fetching password and salt from database", e);
            return false;
        }

        String passwordOnRecord = passwordAndSalt[0].toString();

        String saltOnRecord = passwordAndSalt[1].toString();

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
            log.error("Caught exception while computing salted hash for user {}", userModel.getUsername(), e);
            return false;
        }

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
