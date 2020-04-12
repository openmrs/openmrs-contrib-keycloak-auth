package org.openmrs.keycloak.data;


import org.hibernate.type.StandardBasicTypes;
import org.openmrs.keycloak.models.UserModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    public String getUserPasswordOnRecord(org.keycloak.models.UserModel userModel){
        TypedQuery<UserModel> query = em.createQuery("select u from UserModel u where u.username = :username", UserModel.class);
        query.setParameter("username", userModel.getUsername());
        UserModel user = query.getSingleResult();

        TypedQuery<String> queryPassword = em.
                createQuery("select password from users where user_id = :userId", String.class);
        queryPassword.setParameter("password", StandardBasicTypes.STRING);
        queryPassword.setParameter("userId", user.getUserId());

        return queryPassword.getSingleResult();
    }

    public String getUserSaltOnRecord(org.keycloak.models.UserModel userModel){
        TypedQuery<UserModel> query = em.createQuery("select u from UserModel u where u.username = :username", UserModel.class);
        query.setParameter("username", userModel.getUsername());
        UserModel user = query.getSingleResult();

        TypedQuery<String> querySalt = em.
                createQuery("select salt from users where user_id = :userId", String.class);
        querySalt.setParameter("salt", StandardBasicTypes.STRING);
        querySalt.setParameter("userId", user.getUserId());

        return querySalt.getSingleResult();
    }


}
