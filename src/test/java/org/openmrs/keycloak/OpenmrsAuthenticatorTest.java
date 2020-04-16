package org.openmrs.keycloak;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.provider.OpenmrsAuthenticator;

import javax.inject.Inject;
import java.util.Properties;

public class OpenmrsAuthenticatorTest extends JPAHibernateTest {

    private KeycloakSession session;

    private Properties properties;

    private ComponentModel model;

    @Inject
    private OpenmrsAuthenticator openmrsAuthenticator;


    @Before
    public void setup() {
        openmrsAuthenticator = new OpenmrsAuthenticator(session, properties, model);
    }

    @Test
    public void getUserByUsername() {
    }
}
