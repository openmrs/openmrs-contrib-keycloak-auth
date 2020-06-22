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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.io.InputStreamReader;

import org.h2.tools.RunScript;
import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class JPAHibernateTest {
	
	protected static EntityManagerFactory emf;
	
	protected EntityManager em;
	
	@BeforeClass
	public static void init() {
		emf = Persistence.createEntityManagerFactory("openmrs-persistence-test");
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			Session session = em.unwrap(Session.class);
			session.doWork(connection -> RunScript.execute(connection,
			    new InputStreamReader(JPAHibernateTest.class.getResourceAsStream("/data.sql"))));
			em.getTransaction().commit();
		}
		catch (Exception e) {
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}
	}
	
	@Before
	public void loadEntityManager() {
		em = emf.createEntityManager();
	}
	
	@After
	public void clearEntityManager() {
		em.close();
	}
	
	@AfterClass
	public static void tearDown() {
		emf.close();
	}
}
