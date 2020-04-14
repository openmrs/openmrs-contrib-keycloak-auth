package org.openmrs.keycloak.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "person_name")
public @Data class PersonNameModel {

    @Id
    @Column(name = "person_name_id")
    private Integer personNameId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "person_id", nullable = false)
    private PersonModel person;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "family_name")
    private String familyName;

}
