package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Registration {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Registration Attributes
  @Id
  @GeneratedValue
  private int regId;

  // Registration Associations
  @ManyToOne
  private SpecificClass specificClass;
  @ManyToOne
  private Client client;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  protected Registration() {
  }

  public Registration(int aRegId, SpecificClass aSpecificClass, Client aClient) {
    regId = aRegId;
    if (!setSpecificClass(aSpecificClass)) {
      throw new RuntimeException(
          "Unable to create Registration due to aSpecificClass. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setClient(aClient)) {
      throw new RuntimeException(
          "Unable to create Registration due to aClient. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setRegId(int aRegId) {
    boolean wasSet = false;
    regId = aRegId;
    wasSet = true;
    return wasSet;
  }

  public int getRegId() {
    return regId;
  }

  public SpecificClass getSpecificClass() {
    return specificClass;
  }

  public Client getClient() {
    return client;
  }

  public boolean setSpecificClass(SpecificClass aNewSpecificClass) {
    boolean wasSet = false;
    if (aNewSpecificClass != null) {
      specificClass = aNewSpecificClass;
      wasSet = true;
    }
    return wasSet;
  }

  public boolean setClient(Client aNewClient) {
    boolean wasSet = false;
    if (aNewClient != null) {
      client = aNewClient;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    specificClass = null;
    client = null;
  }

  public String toString() {
    return super.toString() + "[" +
        "regId" + ":" + getRegId() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "specificClass = "
        + (getSpecificClass() != null ? Integer.toHexString(System.identityHashCode(getSpecificClass())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "client = " + (getClient() != null ? Integer.toHexString(System.identityHashCode(getClient())) : "null");
  }
}