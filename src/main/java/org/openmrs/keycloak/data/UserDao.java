package org.openmrs.keycloak.data;


import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.openmrs.keycloak.models.UserModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class UserDao {

    @PersistenceContext(unitName = "openmrs-persistence")
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
