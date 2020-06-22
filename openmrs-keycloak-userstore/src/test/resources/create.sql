CREATE TABLE person (
  person_id int(11) NOT NULL PRIMARY KEY,
  gender varchar(50) NOT NULL);

CREATE TABLE person_name (
  person_name_id int(11) NOT NULL PRIMARY KEY,
  person_id int(11) NOT NULL,
  given_name varchar(255),
  middle_name varchar(255),
  family_name varchar(255));

ALTER TABLE person_name
    ADD FOREIGN KEY (person_id)
    REFERENCES person(person_id);

CREATE TABLE users (
  user_id int(11) NOT NULL PRIMARY KEY,
  person_id int(11) DEFAULT NULL,
  username varchar(255) NOT NULL,
  email varchar(255) DEFAULT NULL,
  password varchar(128) DEFAULT NULL,
  salt varchar(128) DEFAULT NULL);

ALTER TABLE users
    ADD FOREIGN KEY (person_id)
    REFERENCES person (person_id);
