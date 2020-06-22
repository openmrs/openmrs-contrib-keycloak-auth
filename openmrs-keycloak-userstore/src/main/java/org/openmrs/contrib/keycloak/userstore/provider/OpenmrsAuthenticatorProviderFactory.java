/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.contrib.keycloak.userstore.provider;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;
import com.mysql.cj.jdbc.Driver;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL55Dialect;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.tool.schema.Action;
import org.keycloak.component.ComponentModel;
import org.keycloak.component.ComponentValidationException;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.storage.UserStorageProviderFactory;
import org.openmrs.contrib.keycloak.userstore.data.UserDao;
import org.openmrs.contrib.keycloak.userstore.models.OpenmrsUserModel;
import org.openmrs.contrib.keycloak.userstore.models.PersonModel;
import org.openmrs.contrib.keycloak.userstore.models.PersonNameModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenmrsAuthenticatorProviderFactory implements UserStorageProviderFactory<OpenmrsAuthenticator> {
	
	public static final String PROVIDER_NAME = "openmrs-authentication-provider";
	
	private static final Logger log = LoggerFactory.getLogger(OpenmrsAuthenticatorProviderFactory.class);
	
	private static final List<ProviderConfigProperty> CONFIG_METADATA;
	
	static {
		// @formatter:off
		CONFIG_METADATA = ProviderConfigurationBuilder.create()
				.property()
					.name("JDBC URL")
		            .defaultValue("jdbc:mysql://localhost:3306/openmrs?useSSL=false&characterEncoding=UTF-8")
		            .helpText("The JDBC URL for the OpenMRS MySQL Server")
					.type(ProviderConfigProperty.STRING_TYPE)
					.add()
		        .property()
					.name("Username")
					.defaultValue("openmrs")
					.helpText("The user name of the MySQL user")
		            .type(ProviderConfigProperty.STRING_TYPE)
					.add()
				.property()
					.name("Password")
					.defaultValue("openmrs")
		            .helpText("The passsword for the MySQL user")
					.type(ProviderConfigProperty.PASSWORD)
					.secret(true)
					.add()
		        .build();
		// @formatter:on
	}
	
	private volatile EntityManagerFactory emf;
	
	@Override
	public OpenmrsAuthenticator create(KeycloakSession keycloakSession, ComponentModel config) {
		if (emf == null) {
			ensureEntityManagerFactory(config);
		}
		
		return new OpenmrsAuthenticator(keycloakSession, config, new UserDao(emf.createEntityManager()));
	}
	
	@Override
	public void close() {
		if (emf != null) {
			emf.close();
		}
	}
	
	@Override
	public String getId() {
		return PROVIDER_NAME;
	}
	
	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return CONFIG_METADATA;
	}
	
	@Override
	public void validateConfiguration(KeycloakSession session, RealmModel realm, ComponentModel config)
	        throws ComponentValidationException {
		try {
			ensureEntityManagerFactory(config);
			emf.createEntityManager().close();
		}
		catch (PersistenceException e) {
			throw new ComponentValidationException(
			        "An error occurred while trying to validated the supplied connection information the error was: "
			                + e.getLocalizedMessage(),
			        e);
		}
	}
	
	private void ensureEntityManagerFactory(ComponentModel config) {
		if (emf == null) {
			synchronized (this) {
				if (emf == null) {
					emf = new HibernatePersistenceProvider()
					        .createContainerEntityManagerFactory(new PersistenceUnitInfoImpl(),
							// @formatter:off
							ImmutableMap.<String, Object> builder().put(AvailableSettings.JPA_JDBC_DRIVER, Driver.class.getName())
									.put(AvailableSettings.JPA_JDBC_URL, config.get("JDBC URL"))
									.put(AvailableSettings.JPA_JDBC_USER, config.get("Username"))
									.put(AvailableSettings.JPA_JDBC_PASSWORD, config.get("Password")).build());
							// @formatter:on
					
					emf.createEntityManager().close();
				}
			}
			
		}
	}
	
	public static class PersistenceUnitInfoImpl implements PersistenceUnitInfo {
		
		public static final String PERSISTENCE_UNIT_NAME = "openmrs-userstore";
		
		private static final List<String> CLASS_NAMES = Arrays.asList(OpenmrsUserModel.class.getName(),
		    PersonModel.class.getName(), PersonNameModel.class.getName());
		
		private static final Properties SETTINGS = new Properties();
		
		static {
			SETTINGS.setProperty(AvailableSettings.DIALECT, MySQL55Dialect.class.getName());
			SETTINGS.setProperty(AvailableSettings.HBM2DDL_AUTO, Action.VALIDATE.name().toLowerCase());
			SETTINGS.setProperty(AvailableSettings.SHOW_SQL, Boolean.FALSE.toString());
			SETTINGS.setProperty(AvailableSettings.USE_REFLECTION_OPTIMIZER, Boolean.TRUE.toString());
		}
		
		@Override
		public String getPersistenceUnitName() {
			return PERSISTENCE_UNIT_NAME;
		}
		
		@Override
		public String getPersistenceProviderClassName() {
			return HibernatePersistenceProvider.class.getName();
		}
		
		@Override
		public PersistenceUnitTransactionType getTransactionType() {
			return PersistenceUnitTransactionType.RESOURCE_LOCAL;
		}
		
		@Override
		public DataSource getJtaDataSource() {
			return null;
		}
		
		@Override
		public DataSource getNonJtaDataSource() {
			return null;
		}
		
		@Override
		public List<String> getMappingFileNames() {
			return null;
		}
		
		@Override
		public List<URL> getJarFileUrls() {
			return null;
		}
		
		@Override
		public URL getPersistenceUnitRootUrl() {
			return null;
		}
		
		@Override
		public List<String> getManagedClassNames() {
			return CLASS_NAMES;
		}
		
		@Override
		public boolean excludeUnlistedClasses() {
			return false;
		}
		
		@Override
		public SharedCacheMode getSharedCacheMode() {
			return SharedCacheMode.UNSPECIFIED;
		}
		
		@Override
		public ValidationMode getValidationMode() {
			return ValidationMode.AUTO;
		}
		
		@Override
		public Properties getProperties() {
			return SETTINGS;
		}
		
		@Override
		public String getPersistenceXMLSchemaVersion() {
			return null;
		}
		
		@Override
		public ClassLoader getClassLoader() {
			return null;
		}
		
		@Override
		public void addTransformer(ClassTransformer transformer) {
			
		}
		
		@Override
		public ClassLoader getNewTempClassLoader() {
			return null;
		}
	}
}
