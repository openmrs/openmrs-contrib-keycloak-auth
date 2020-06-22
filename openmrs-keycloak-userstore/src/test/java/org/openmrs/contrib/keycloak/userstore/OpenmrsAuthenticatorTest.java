/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.contrib.keycloak.userstore;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.openmrs.contrib.keycloak.userstore.data.UserDao;
import org.openmrs.contrib.keycloak.userstore.models.OpenmrsUserModel;
import org.openmrs.contrib.keycloak.userstore.provider.OpenmrsAuthenticator;

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
	
	private OpenmrsUserModel openmrsUserModel;
	
	private OpenmrsAuthenticator openmrsAuthenticator;
	
	@Before
	public void setup() {
		openmrsAuthenticator = new OpenmrsAuthenticator(session, model, userDao);
		
		openmrsUserModel = new OpenmrsUserModel();
		openmrsUserModel.setUsername("admin");
	}
	
	@Test
	public void getUserByUsername() {
		when(userDao.getOpenmrsUserByUsername("admin")).thenReturn(openmrsUserModel);
		
		UserModel result = openmrsAuthenticator.getUserByUsername("admin", realmModel);
		
		assertThat(result, notNullValue());
		assertThat(result.getUsername(), notNullValue());
		assertThat(result.getUsername(), equalTo("admin"));
	}
}
