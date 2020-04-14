package org.openmrs.keycloak;

import org.junit.Test;
import org.openmrs.keycloak.models.UserModel;

import javax.persistence.TypedQuery;
import java.util.List;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JPAHibernateCRUDTest extends JPAHibernateTest {

    @Test
    public void getUserByUsername()
    {
        UserModel query = em.createQuery("select u from UserModel u where u.username = admin", UserModel.class).getSingleResult();
        assertEquals("admin",query.getUsername());

    }

    @Test
    public void testGetObjectById_success() {
        UserModel userModel = em.find(UserModel.class, 1);
        assertNotNull(userModel);
    }

//    @Test
//    public void testGetAll_success() {
//        List<Book> books = em.createNamedQuery("Book.getAll", Book.class).getResultList();
//        assertEquals(1, books.size());
//    }

//    @Test
//    public void testPersist_success() {
//        em.getTransaction().begin();
//        em.persist(new UserModel(10, "Unit Test Hibernate/JPA with in memory H2 Database"));
//        em.getTransaction().commit();
//
//        List<Book> books = em.createNamedQuery("Book.getAll", Book.class).getResultList();
//
//        assertNotNull(books);
//        assertEquals(2, books.size());
//    }

//    @Test
//    public void testDelete_success(){
//        Book book = em.find(Book.class, 1);
//
//        em.getTransaction().begin();
//        em.remove(book);
//        em.getTransaction().commit();
//
//        List<Book> books = em.createNamedQuery("Book.getAll", Book.class).getResultList();
//
//        assertEquals(0, books.size());
//    }

}