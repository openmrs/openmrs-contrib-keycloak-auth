package org.openmrs.keycloak.data;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class UserDAO {

    @PersistenceContext
    protected EntityManager em;

    public UserModel getUserByUsername(UserModel user) {
        Query query = em.createQuery("select u from UserModel u where u.username = :username", UserModel.class);
        query.setParameter("username", user.getUsername());
        return (UserModel) query.getSingleResult();
    }

    public UserModel getUserByUserGivenName(PersonNameModel personName) {
        Query query = em.createQuery("select u from UserModel, PersonNameModel pn  where pn.givenName = :givenName and pn.personNameId=u.userId", UserModel.class);
        query.setParameter("givenName", personName.getGivenName());
        return (UserModel) query.getSingleResult();
    }
}
