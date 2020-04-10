package org.openmrs.keycloak.data;


import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.keycloak.OpenmrsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class UserDAO {

    @PersistenceContext
    private EntityManager em;

    public UserModel getUserByUsername(String username) {
        TypedQuery<UserModel> query = em.createQuery("select u from UserModel u where u.username = :username", UserModel.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public UserModel getUserByUserId(Integer userId) {
        TypedQuery<UserModel> query = em.createQuery("select u from UserModel u where u.userId = :userId", UserModel.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public UserModel getUserByEmail(String email) throws NotImplementedException {
        TypedQuery<UserModel> query = em.createQuery("select u from UserModel u where u.email = :email", UserModel.class);
        query.setParameter("userId", email);
        return query.getSingleResult();
    }
}
