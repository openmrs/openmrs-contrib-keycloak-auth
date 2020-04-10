package org.openmrs.keycloak;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.openmrs.keycloak.data.Security;
import org.openmrs.keycloak.data.UserDAO;
import org.openmrs.keycloak.data.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenmrsAuthentication {

    private SessionFactory sessionFactory;

    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    public UserModel authenticate(String login, String password) {

        String errorMsg = "Invalid username and/or password: " + login;

        Session session = sessionFactory.getCurrentSession();

        UserModel candidateUser = null;

        if (login != null) {
            //if username is blank or white space character(s)
            if (StringUtils.isEmpty(login) || StringUtils.isWhitespace(login)) {
                throw new RuntimeException(errorMsg);
            }

            // loginWithoutDash is used to compare to the system id
            String loginWithDash = login;
            if (login.matches("\\d{2,}")) {
                loginWithDash = login.substring(0, login.length() - 1) + "-" + login.charAt(login.length() - 1);
            }

            try {
                candidateUser = (UserModel) session.createQuery(
                        "from User u where (u.username = ?0 or u.systemId = ?1 or u.systemId = ?2) and u.retired = '0'").setString(
                        0, login).setString(1, login).setString(2, loginWithDash).uniqueResult();
            }
            catch (HibernateException he) {
                log.error("Got hibernate exception while logging in: '" + login + "'", he);
            }
            catch (Exception e) {
                log.error("Got regular exception while logging in: '" + login + "'", e);
            }
        }

        // only continue if this is a valid username and a nonempty password
        if (candidateUser != null && password != null) {
            if (log.isDebugEnabled()) {
                log.debug("Candidate user id: " + candidateUser.getUserId());
            }

            String passwordOnRecord = (String) session.createSQLQuery("select password from users where user_id = ?0")
                    .addScalar("password", StandardBasicTypes.STRING).setInteger(0, candidateUser.getUserId())
                    .uniqueResult();

            String saltOnRecord = (String) session.createSQLQuery("select salt from users where user_id = ?0").addScalar(
                    "salt", StandardBasicTypes.STRING).setInteger(0, candidateUser.getUserId()).uniqueResult();

            // if the username and password match, hydrate the user and return it
            if (passwordOnRecord != null && Security.hashMatches(passwordOnRecord, password + saltOnRecord)) {
                // skip out of the method early (instead of throwing the exception)
                // to indicate that this is the valid user
                return candidateUser;
            }
        }

        // throw this exception only once in the same place with the same
        // message regardless of username/pw combo entered
        log.info("Failed login attempt (login=" + login + ") - " + errorMsg);
        throw new RuntimeException(errorMsg);
    }
}
