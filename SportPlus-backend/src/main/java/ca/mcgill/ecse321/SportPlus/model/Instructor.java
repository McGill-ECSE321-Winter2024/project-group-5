package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Instructor")
public class Instructor extends Account {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Instructor() {
    super();
  }

  public Instructor(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId) {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId);
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public void delete() {
    super.delete();
  }

}