package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 15 "model.ump"
// line 67 "model.ump"
// line 99 "model.ump"
@Entity
public class Client extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Client() {
    super();
  }


  public Client(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId)
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