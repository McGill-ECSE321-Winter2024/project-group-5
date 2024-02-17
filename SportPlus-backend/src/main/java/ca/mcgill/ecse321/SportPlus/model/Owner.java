package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;

@Entity
public class Owner extends Account
{


  public Owner(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId, SportPlus aSportPlus)
  {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId, aSportPlus);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}