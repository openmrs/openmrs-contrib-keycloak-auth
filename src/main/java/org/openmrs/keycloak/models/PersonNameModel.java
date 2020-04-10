package org.openmrs.keycloak.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "person_name")
public @Data class PersonNameModel {

    @Id
    private Integer personNameId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonModel person;

    private String givenName;

    private String middleName;

    private String familyName;

}
