package org.openmrs.keycloak.data;


import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.openmrs.keycloak.models.OpenmrsUserModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class UserDao {

    @PersistenceContext(unitName = "openmrs-persistence")
    private EntityManager em;

    public OpenmrsUserModel getMrsUserByUsername(String username) {
        TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.username = :username", OpenmrsUserModel.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

    public OpenmrsUserModel getMrsUserByUserId(Integer userId) {
        TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.userId = :userId", OpenmrsUserModel.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

    public OpenmrsUserModel getMrsUserByEmail(String email) throws NotImplementedException {
        TypedQuery<OpenmrsUserModel> query = em.createQuery("select u from OpenmrsUserModel u where u.email = :email", OpenmrsUserModel.class);
        query.setParameter("userId", email);
        return query.getSingleResult();
    }

    public Object[] getUserPasswordAndSaltOnRecord(org.keycloak.models.UserModel userModel) {
        String username = userModel.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        Query query = em.createNativeQuery("select password, salt from users u where u.username = :username");
        query.setParameter("username", userModel.getUsername());
        return (Object[]) query.getSingleResult();
    }
}
