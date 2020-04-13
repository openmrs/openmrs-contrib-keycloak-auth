package org.openmrs.keycloak.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class OpenmrsAuthenticatorProviderFactory
                 implements UserStorageProviderFactory<OpenmrsAuthenticator> {

    public static final String PROVIDER_NAME = "smart-openmrs";

    private static final Logger logger = LoggerFactory.getLogger(OpenmrsAuthenticatorProviderFactory.class);

    protected Properties properties = new Properties();

    @Override
    public OpenmrsAuthenticator create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new OpenmrsAuthenticator(keycloakSession,properties,componentModel);
    }

    @Override
    public String getId() {
        return PROVIDER_NAME;
    }
}