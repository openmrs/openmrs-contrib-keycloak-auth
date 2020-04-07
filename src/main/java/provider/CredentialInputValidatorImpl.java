package provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CredentialInputValidatorImpl implements CredentialInputValidator {

    protected KeycloakSession session;
    protected Properties properties;
    protected ComponentModel model;

    // map of loaded users in this transaction
    protected Map<String, UserModel> loadedUsers = new HashMap<String, UserModel>();

    public CredentialInputValidatorImpl(KeycloakSession session, Properties properties, ComponentModel model, Map<String, UserModel> loadedUsers) {
        this.session = session;
        this.properties = properties;
        this.model = model;
        this.loadedUsers = loadedUsers;
    }

    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(CredentialModel.PASSWORD);

    }

    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
        String password = properties.getProperty(userModel.getUsername());
        return credentialType.equals(CredentialModel.PASSWORD) && password != null;

    }

    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        if (!supportsCredentialType(credentialInput.getType()))
            return false;

        String password = properties.getProperty(userModel.getUsername());
        if (password == null) return false;
        return password.equals(credentialInput.getChallengeResponse());

    }
}
