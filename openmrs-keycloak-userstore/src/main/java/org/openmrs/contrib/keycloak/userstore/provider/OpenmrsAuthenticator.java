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

import javax.persistence.PersistenceException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Setter;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.openmrs.contrib.keycloak.userstore.data.UserAdapter;
import org.openmrs.contrib.keycloak.userstore.data.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Setter(AccessLevel.PACKAGE)
public class OpenmrsAuthenticator implements CredentialInputValidator, UserLookupProvider, UserStorageProvider, UserQueryProvider {
	
	protected static final MessageDigest MESSAGE_DIGEST;
	
	private static final Logger log = LoggerFactory.getLogger(OpenmrsAuthenticator.class);
	
	static {
		try {
			MESSAGE_DIGEST = MessageDigest.getInstance("SHA-512");
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected KeycloakSession session;
	
	protected ComponentModel model;
	
	protected UserDao userDao;
	
	public OpenmrsAuthenticator(KeycloakSession session, ComponentModel model, UserDao userDao) {
		this.session = session;
		this.model = model;
		this.userDao = userDao;
	}
	
	@Override
	public UserModel getUserById(String id, RealmModel realmModel) {
		return new UserAdapter(session, realmModel, model,
		        userDao.getOpenmrsUserByUserId(Integer.parseInt(StorageId.externalId(id))));
	}
	
	@Override
	public UserModel getUserByUsername(String username, RealmModel realmModel) {
		return new UserAdapter(session, realmModel, model, userDao.getOpenmrsUserByUsername(username));
	}
	
	@Override
	public UserModel getUserByEmail(String email, RealmModel realmModel) {
		return new UserAdapter(session, realmModel, model, userDao.getOpenmrsUserByEmail(email));
	}
	
	@Override
	public boolean supportsCredentialType(String credentialType) {
		return credentialType.equals(PasswordCredentialModel.TYPE);
	}
	
	@Override
	public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String credentialType) {
		return credentialType.equals(PasswordCredentialModel.TYPE);
	}
	
	@Override
	public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
		if (!(credentialInput instanceof UserCredentialModel) || !supportsCredentialType(credentialInput.getType())) {
			return false;
		}
		
		String[] passwordAndSalt;
		try {
			passwordAndSalt = userDao.getUserPasswordAndSaltOnRecord(userModel);
		}
		catch (PersistenceException e) {
			log.error("Caught exception while fetching password and salt from database", e);
			return false;
		}
		
		String passwordOnRecord = passwordAndSalt[0];
		String saltOnRecord = passwordAndSalt[1];
		String currentPassword = credentialInput.getChallengeResponse();
		
		if (passwordOnRecord == null || saltOnRecord == null || currentPassword == null) {
			return false;
		}
		
		String passwordToHash = currentPassword + saltOnRecord;
		byte[] input = passwordToHash.getBytes(StandardCharsets.UTF_8);
		return passwordOnRecord.equals(hexString(MESSAGE_DIGEST.digest(input)));
	}
	
	private String hexString(byte[] block) {
		StringBuilder buf = new StringBuilder();
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
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
	
	@Override
	public int getUsersCount(RealmModel realmModel) {
		return userDao.getOpenmrsUserCount();
	}
	
	@Override
	public List<UserModel> getUsers(RealmModel realmModel) {
		return getUsers(realmModel, 0, Integer.MAX_VALUE);
	}
	
	@Override
	public List<UserModel> getUsers(RealmModel realmModel, int firstResult, int maxResults) {
		return userDao.getAllOpenmrsUsers(firstResult, maxResults).stream()
		        .map(userModel -> new UserAdapter(session, realmModel, model, userModel)).collect(Collectors.toList());
	}
	
	@Override
	public List<UserModel> searchForUser(String s, RealmModel realmModel) {
		return searchForUser(s, realmModel, 0, Integer.MAX_VALUE);
	}
	
	@Override
	public List<UserModel> searchForUser(String search, RealmModel realmModel, int firstResult, int maxResults) {
		return searchForUser(ImmutableMap.<String, String> builder().put("username", search).put("email", search)
		        .put("first", search).put("last", search).build(),
		    realmModel, firstResult, maxResults);
	}
	
	@Override
	public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) {
		return searchForUser(map, realmModel, 0, Integer.MAX_VALUE);
	}
	
	@Override
	public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int firstResult, int maxResults) {
		return userDao.searchForOpenmrsUserQuery(map, firstResult, maxResults).stream()
		        .map(userModel -> new UserAdapter(session, realmModel, model, userModel)).collect(Collectors.toList());
	}
	
	@Override
	public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i, int i1) {
		return Collections.emptyList();
	}
	
	@Override
	public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) {
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) {
		return Collections.emptyList();
	}
}
