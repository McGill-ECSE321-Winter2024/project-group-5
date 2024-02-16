package ca.mcgill.ecse321.SportPlus.model;

/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


// line 2 "model.ump"
// line 59 "model.ump"
// line 92 "model.ump"
@Entity
public abstract class Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Account Attributes
  @Id
  private int userId;

  private String email;
  private String firstname;
  
  private String password;
  private String lastName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Account(String aEmail, String aFirstname, int aUserId, String aPassword, String aLastName)
  {
    email = aEmail;
    firstname = aFirstname;
    userId = aUserId;
    password = aPassword;
    lastName = aLastName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setFirstname(String aFirstname)
  {
    boolean wasSet = false;
    firstname = aFirstname;
    wasSet = true;
    return wasSet;
  }

  public boolean setUserId(int aUserId)
  {
    boolean wasSet = false;
    userId = aUserId;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setLastName(String aLastName)
  {
    boolean wasSet = false;
    lastName = aLastName;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getFirstname()
  {
    return firstname;
  }

  public int getUserId()
  {
    return userId;
  }

  public String getPassword()
  {
    return password;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "firstname" + ":" + getFirstname()+ "," +
            "userId" + ":" + getUserId()+ "," +
            "password" + ":" + getPassword()+ "," +
            "lastName" + ":" + getLastName()+ "]";
  }
}