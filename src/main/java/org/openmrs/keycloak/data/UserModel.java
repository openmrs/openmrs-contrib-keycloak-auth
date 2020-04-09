package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public @Data class UserModel {
    @Id
    private Integer userId;

    private PersonModel person;

    private String username;

    private String email;

}
