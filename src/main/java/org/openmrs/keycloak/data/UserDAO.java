package org.openmrs.keycloak.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO {

    private SessionFactory sessionFactory;

    public UserModel getUserByUsername(UserModel user){
        String queryString = "FROM org.openmrs.keycloak.data.UserModel where user.userId = :username";//How to write these?
        Session session=sessionFactory.getCurrentSession();
        Query query=session.createQuery(queryString);
        query.setParameter("",user.getUsername());
        List<UserModel> userModelList=query.list();
        return userModelList.get(0);
    }
}
