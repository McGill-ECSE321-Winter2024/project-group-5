package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Owner")
public class Owner extends Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Owner() {
    super();
  }

  public Owner(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId) {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId);
  }

  public void delete() {
    super.delete();
  }

}