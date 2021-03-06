package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.ResourceSupport;

import java.sql.Timestamp;

/**
 * Created by cjemison on 1/28/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonVO extends ResourceSupport {
  private Integer personId;
  private String firstName;
  private String lastName;
  private String emailAddress;
  private Timestamp createdDate;
  private Timestamp updatedDate;

  public PersonVO() {
  }

  public PersonVO(final Integer personId,
                  final String firstName,
                  final String lastName,
                  final String emailAddress,
                  final Timestamp createdDate,
                  final Timestamp updatedDate) {
    this.personId = personId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailAddress = emailAddress;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }

  public Integer getPersonId() {
    return personId;
  }

  public void setPersonId(final Integer personId) {
    this.personId = personId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(final Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(final Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof PersonVO)) return false;

    final PersonVO personVO = (PersonVO) o;

    return getId() != null ? getId().equals(personVO.getId()) : personVO.getId() == null;
  }

  @Override
  public int hashCode() {
    return getId() != null ? getId().hashCode() : 0;
  }

  @Override
  public String toString() {
    return "PersonVO{" +
          "personId=" + personId +
          ", firstName='" + firstName + '\'' +
          ", lastName='" + lastName + '\'' +
          ", emailAddress='" + emailAddress + '\'' +
          ", createdDate=" + createdDate +
          ", updatedDate=" + updatedDate +
          '}';
  }
}
