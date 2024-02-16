package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;

@Entity
public class Instructor extends Account
{


  public Instructor(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId, SportPlus aSportPlus)
  {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId, aSportPlus);
  }


  public void delete()
  {
    super.delete();
  }

}