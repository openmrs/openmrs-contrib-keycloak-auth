/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.contrib.keycloak.userstore.data;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.contrib.keycloak.userstore.models.OpenmrsUserModel;

public class UserDao {
	
	private final EntityManager em;
	
	public UserDao(EntityManager em) {
		this.em = em;
	}
	
	public OpenmrsUserModel getOpenmrsUserByUsername(String username) {
		TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.username = :username",
		    OpenmrsUserModel.class);
		query.setParameter("username", username);
		return query.getSingleResult();
	}
	
	public OpenmrsUserModel getOpenmrsUserByUserId(Integer userId) {
		TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.userId = :userId",
		    OpenmrsUserModel.class);
		query.setParameter("userId", userId);
		return query.getSingleResult();
	}
	
	public OpenmrsUserModel getOpenmrsUserByEmail(String email) throws NotImplementedException {
		TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.email = :email",
		    OpenmrsUserModel.class);
		query.setParameter("email", email);
		return query.getSingleResult();
	}
	
	public String[] getUserPasswordAndSaltOnRecord(org.keycloak.models.UserModel userModel) {
		String username = userModel.getUsername();
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("Username cannot be blank");
		}
		
		Query query = em.createNativeQuery("select password, salt from users u where u.username = :username");
		query.setParameter("username", userModel.getUsername());
		return Arrays.stream((Object[]) query.getSingleResult()).map(Object::toString).toArray(String[]::new);
	}
	
	public int getOpenmrsUserCount() {
		Number count = em.createQuery("select count(u) from OpenmrsUserModel u", Long.class).getSingleResult();
		return count.intValue();
	}
	
	public List<OpenmrsUserModel> getAllOpenmrsUsers(int firstResult, int maxResult) {
		return em.createQuery("select u from OpenmrsUserModel u", OpenmrsUserModel.class).setFirstResult(firstResult)
		        .setMaxResults(maxResult).getResultList();
	}
	
	public List<OpenmrsUserModel> searchForOpenmrsUserQuery(Map<String, String> map, int firstResult, int maxResult) {
		return em
		        .createQuery("select u from OpenmrsUserModel u left outer join u.person.names n "
		                + "where (:username is null or lower(u.username) like lower(:username)) or "
		                + "(:email is null or lower(u.email) like lower(:email)) or "
		                + "(:first is null or lower(n.givenName) like lower(:first)) or "
		                + "(:last is null or lower(n.familyName) like lower(:last))",
		            OpenmrsUserModel.class)
		        .setParameter("username", map.get("username")).setParameter("email", map.get("email"))
		        .setParameter("first", map.get("first")).setParameter("last", map.get("last")).setFirstResult(firstResult)
		        .setMaxResults(maxResult).getResultList();
	}
}
