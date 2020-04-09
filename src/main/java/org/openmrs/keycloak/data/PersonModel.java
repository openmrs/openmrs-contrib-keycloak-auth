package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "person")
public @Data class PersonModel {

    @Id
    protected Integer personId;

    @OneToMany
    private Set <PersonNameModel> names;

    private String gender;
}
