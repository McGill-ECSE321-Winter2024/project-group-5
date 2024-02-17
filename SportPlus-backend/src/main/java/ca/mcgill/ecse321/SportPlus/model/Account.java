package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/

import jakarta.persistence.DiscriminatorColumn;

/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype") // This line is crucial
public abstract class Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Account Attributes
  private String email;
  private String firstName;
  private String password;
  private String lastName;

  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @GeneratedValue
  private int accountId;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------
  protected Account() {

  }

  public Account(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId) {
    email = aEmail;
    firstName = aFirstName;
    password = aPassword;
    lastName = aLastName;
    accountId = aAccountId;
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setEmail(String aEmail) {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setFirstName(String aFirstName) {
    boolean wasSet = false;
    firstName = aFirstName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setLastName(String aLastName) {
    boolean wasSet = false;
    lastName = aLastName;
    wasSet = true;
    return wasSet;
  }

  public boolean setAccountId(int aAccountId) {
    boolean wasSet = false;
    accountId = aAccountId;
    wasSet = true;
    return wasSet;
  }

  public String getEmail() {
    return email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getPassword() {
    return password;
  }

  public String getLastName() {
    return lastName;
  }

  public int getAccountId() {
    return accountId;
  }

  public void delete() {
  }

  public String toString() {
    return super.toString() + "[" +
        "email" + ":" + getEmail() + "," +
        "firstName" + ":" + getFirstName() + "," +
        "password" + ":" + getPassword() + "," +
        "lastName" + ":" + getLastName() + "," +
        "accountId" + ":" + getAccountId() + "]";
  }
}