package org.openmrs.keycloak;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.keycloak.data.UserAdapter;
import org.openmrs.keycloak.data.UserDao;
import org.openmrs.keycloak.models.OpenmrsUserModel;
import org.openmrs.keycloak.provider.OpenmrsAuthenticator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenmrsAuthenticatorTest extends JPAHibernateTest {

    @Mock
    private KeycloakSession session;

    @Mock
    private ComponentModel model;

    @Mock
    private UserDao userDao;

    @Mock
    private RealmModel realmModel;

    private UserAdapter userAdapter;


    @Mock
    private ComponentModel storageProviderModel;

    private OpenmrsUserModel openmrsUserModel;

    private OpenmrsAuthenticator openmrsAuthenticator;


    @Before
    public void setup() {
        openmrsAuthenticator = new OpenmrsAuthenticator(session, model, userDao);

        openmrsUserModel = new OpenmrsUserModel();
        openmrsUserModel.setUsername("admin");

        userAdapter = new UserAdapter(session, realmModel, storageProviderModel, openmrsUserModel);
        userAdapter.setUsername("admin");
    }

    @Test
    public void getUserByUsername() {
        when(userDao.getMrsUserByUsername("admin")).thenReturn(openmrsUserModel);

//        when(openmrsAuthenticator.getUserByUsername("admin", realmModel)).thenReturn(userAdapter);

        UserModel result = openmrsAuthenticator.getUserByUsername("admin", realmModel);

        assertThat(result, notNullValue());
        assertThat(result.getUsername(), notNullValue());
        assertThat(result.getUsername(), equalTo("admin"));
    }
}
