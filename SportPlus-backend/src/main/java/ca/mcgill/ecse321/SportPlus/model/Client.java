package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// line 15 "model.ump"
// line 64 "model.ump"
// line 97 "model.ump"
@Entity
public class Client extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Client(String aEmail, String aFirstname, int aUserId, String aPassword, String aLastName)
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