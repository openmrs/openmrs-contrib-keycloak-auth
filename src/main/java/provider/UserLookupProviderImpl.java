package provider;

import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.user.UserLookupProvider;

public class UserLookupProviderImpl implements UserLookupProvider {
    public UserModel getUserById(String s, RealmModel realmModel) {
        return null;
    }

    public UserModel getUserByUsername(String s, RealmModel realmModel) {
        return null;
    }

    public UserModel getUserByEmail(String s, RealmModel realmModel) {
        return null;
    }
}
