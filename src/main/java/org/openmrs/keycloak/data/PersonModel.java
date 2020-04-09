package org.openmrs.keycloak.data;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public @Data class PersonModel {

    @Id
    protected Integer personId;

    private PersonNameModel names;

    private String gender;
}
