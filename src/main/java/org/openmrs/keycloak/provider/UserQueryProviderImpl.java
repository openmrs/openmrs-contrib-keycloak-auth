package org.openmrs.keycloak.provider;

import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.user.UserQueryProvider;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserQueryProviderImpl implements UserQueryProvider {
    public int getUsersCount(RealmModel realmModel) {
        return 0;
    }

    public int getUsersCount(RealmModel realm, Set<String> groupIds) {
        return 0;
    }

    public int getUsersCount(String search, RealmModel realm) {
        return 0;
    }

    public int getUsersCount(String search, RealmModel realm, Set<String> groupIds) {
        return 0;
    }

    public int getUsersCount(Map<String, String> params, RealmModel realm) {
        return 0;
    }

    public int getUsersCount(Map<String, String> params, RealmModel realm, Set<String> groupIds) {
        return 0;
    }

    public int getUsersCount(RealmModel realm, boolean includeServiceAccount) {
        return 0;
    }

    public List<UserModel> getUsers(RealmModel realmModel) {
        return null;
    }

    public List<UserModel> getUsers(RealmModel realmModel, int i, int i1) {
        return null;
    }

    public List<UserModel> searchForUser(String s, RealmModel realmModel) {
        return null;
    }

    public List<UserModel> searchForUser(String s, RealmModel realmModel, int i, int i1) {
        return null;
    }

    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel) {
        return null;
    }

    public List<UserModel> searchForUser(Map<String, String> map, RealmModel realmModel, int i, int i1) {
        return null;
    }

    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel, int i, int i1) {
        return null;
    }

    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role) {
        return null;
    }

    public List<UserModel> getRoleMembers(RealmModel realm, RoleModel role, int firstResult, int maxResults) {
        return null;
    }

    public List<UserModel> getGroupMembers(RealmModel realmModel, GroupModel groupModel) {
        return null;
    }

    public List<UserModel> searchForUserByUserAttribute(String s, String s1, RealmModel realmModel) {
        return null;
    }
}
