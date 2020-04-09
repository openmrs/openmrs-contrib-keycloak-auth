package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person_name")
public @Data class PersonNameModel {

    @Id
    private Integer personNameId;

    @ManyToOne
    private PersonModel person;

    private String givenName;

    private String middleName;

    private String familyName;

}
