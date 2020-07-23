/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.contrib.keycloak.userstore.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "person_name")
@Data
public class PersonNameModel {
	
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
