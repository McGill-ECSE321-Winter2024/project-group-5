package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

@Entity
@DiscriminatorValue("Client")
public class Client extends Account {

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Client() {
    super();
  }

  public Client(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId) {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId);
  }

  public void delete() {
    super.delete();
  }

}