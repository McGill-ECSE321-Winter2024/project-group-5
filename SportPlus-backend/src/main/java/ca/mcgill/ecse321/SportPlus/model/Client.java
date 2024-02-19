package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

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