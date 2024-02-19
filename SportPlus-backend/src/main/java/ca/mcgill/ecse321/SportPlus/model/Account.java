package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Account Attributes
  @Id
  @GeneratedValue
  private int accountId;
  private String email;
  private String firstName;
  private String password;
  private String lastName;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  protected Account() {}

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