package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 11 "model.ump"
// line 63 "model.ump"
// line 95 "model.ump"
@Entity
public class Instructor extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Instructor(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId)
  {
    super(aEmail, aFirstName, aPassword, aLastName, aAccountId);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public void delete()
  {
    super.delete();
  }

}