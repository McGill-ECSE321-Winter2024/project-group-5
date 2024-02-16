package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


// line 22 "model.ump"
// line 71 "model.ump"
// line 104 "model.ump"
@Entity
public class Owner extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Owner(String aEmail, String aFirstname, int aUserId, String aPassword, String aLastName)
  {
    super(aEmail, aFirstname, aUserId, aPassword, aLastName);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}