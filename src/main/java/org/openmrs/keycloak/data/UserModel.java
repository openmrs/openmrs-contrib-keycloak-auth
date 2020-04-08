package org.openmrs.keycloak.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@NamedQueries({
        @NamedQuery(name = "getUserByUsername", query = "select u from UserModel u where u.username = :username"),
        @NamedQuery(name = "getUserByEmail", query = "select u from UserModel u where u.email = :email"),
        @NamedQuery(name = "getUserCount", query = "select count(u) from UserModel u"),
        @NamedQuery(name = "getAllUsers", query = "select u from UserModel u"),
        @NamedQuery(name = "searchForUser", query = "select u from UserModel u where " +
                "( lower(u.username) like :search or u.email like :search ) order by u.username"),
})
@Entity
public class UserModel {
    @Id
    private Integer userId;

//    private Person person;

    private String systemId;

    private String username;

    private String email;

//    private Set<Role> roles;

    private Map<String, String> userProperties;

    private List<Locale> proficientLocales = null;

    private String parsedProficientLocalesProperty = "";

    public UserModel() {
    }

    public UserModel(Integer userId, String systemId, String username, String email, Map<String, String> userProperties, List<Locale> proficientLocales, String parsedProficientLocalesProperty) {
        this.userId = userId;
        this.systemId = systemId;
        this.username = username;
        this.email = email;
        this.userProperties = userProperties;
        this.proficientLocales = proficientLocales;
        this.parsedProficientLocalesProperty = parsedProficientLocalesProperty;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(Map<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    public List<Locale> getProficientLocales() {
        return proficientLocales;
    }

    public void setProficientLocales(List<Locale> proficientLocales) {
        this.proficientLocales = proficientLocales;
    }

    public String getParsedProficientLocalesProperty() {
        return parsedProficientLocalesProperty;
    }

    public void setParsedProficientLocalesProperty(String parsedProficientLocalesProperty) {
        this.parsedProficientLocalesProperty = parsedProficientLocalesProperty;
    }
}
