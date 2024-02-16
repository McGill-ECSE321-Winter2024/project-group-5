package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;

@Entity
public class Client extends Account
{


  public Client(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId, SportPlus aSportPlus)
  {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId, aSportPlus);
  }


  public void delete()
  {
    super.delete();
  }

}