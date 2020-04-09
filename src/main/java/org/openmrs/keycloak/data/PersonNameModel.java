package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public @Data class PersonNameModel {

    @Id
    private Integer personNameId;

    private PersonModel person;

    private String prefix;

    private String givenName;

    private String middleName;

    private String familyName;

}
