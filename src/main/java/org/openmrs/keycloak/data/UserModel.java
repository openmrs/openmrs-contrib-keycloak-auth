package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public @Data class UserModel {
    @Id
    private Integer userId;

    private PersonModel person;

    private String username;

    private String email;

}
