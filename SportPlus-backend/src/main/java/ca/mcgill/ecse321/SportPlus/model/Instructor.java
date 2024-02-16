package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


// line 11 "model.ump"
// line 63 "model.ump"
// line 96 "model.ump"
@Entity
public class Instructor extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Instructor(String aEmail, String aFirstname, int aUserId, String aPassword, String aLastName)
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