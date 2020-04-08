package org.openmrs.keycloak.data;

import org.openmrs.User;
import org.openmrs.api.db.ContextDAO;


public class UserCredentialsDAO {

    ContextDAO contextDAO;

    public User authenticate(String username, String password) {
        User user = null;
        user = contextDAO.authenticate(username, password);
        return user;
    }
}
