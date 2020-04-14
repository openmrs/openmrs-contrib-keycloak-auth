package org.openmrs.keycloak.models;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "person")
public @Data
class PersonModel {

    @Id
    @Column(name = "person_id")
    protected Integer personId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "person", orphanRemoval = true)
    private Set<PersonNameModel> names;

    private String gender;
}
