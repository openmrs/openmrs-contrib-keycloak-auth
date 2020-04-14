package org.openmrs.keycloak;

import org.junit.Test;
import org.openmrs.keycloak.models.UserModel;

import javax.persistence.Query;

import java.util.List;

import static org.junit.Assert.*;

public class JPAHibernateCRUDTest extends JPAHibernateTest {

    @Test
    public void getUserByUsername() {
        UserModel query = em.createQuery("select u from UserModel u where u.username = 'admin'", UserModel.class).getSingleResult();
        assertEquals("admin",query.getUsername());
        assertTrue("Error, random is too low",  query.getUserId()  == 152);

        UserModel query2 = em.createQuery("select u from UserModel u where u.userId = '186'", UserModel.class).getSingleResult();
        assertEquals("Sid",query2.getUsername());

        Query query3 = em.createNativeQuery("select password, salt from users u where u.username = 'SidVaish'");
        Object[] result= (Object[]) query3.getSingleResult();
        assertEquals("123",result[1].toString());
    }

}
