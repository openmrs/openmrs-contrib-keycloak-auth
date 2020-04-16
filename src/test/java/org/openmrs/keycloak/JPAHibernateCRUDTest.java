package org.openmrs.keycloak;

import org.junit.Test;
import org.openmrs.keycloak.models.OpenmrsUserModel;

import javax.persistence.Query;

import static org.junit.Assert.*;

public class JPAHibernateCRUDTest extends JPAHibernateTest {

    @Test
    public void getUserByUsername() {
        OpenmrsUserModel query = em.createQuery("select u from OpenmrsUserModel u where u.username = 'admin'", OpenmrsUserModel.class).getSingleResult();
        assertEquals("admin",query.getUsername());
        assertTrue("Error, random is too low",  query.getUserId()  == 152);
    }

    @Test
    public void getUserById()
    {
        OpenmrsUserModel query = em.createQuery("select u from OpenmrsUserModel u where u.userId = '186'", OpenmrsUserModel.class).getSingleResult();
        assertEquals("Sid",query.getUsername());
    }

    @Test
    public void getPasswordAndSalt()
    {
        Query query = em.createNativeQuery("select password, salt from users u where u.username = 'SidVaish'");
        Object[] result= (Object[]) query.getSingleResult();
        assertEquals("123",result[1].toString());
    }

}
