
package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account
{
  @Id
  @GeneratedValue
  private int accountId;
  private String email;
  private String firstName;
  private String password;
  private String lastName;
  

  //Account Associations
  private SportPlus sportPlus;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Account(String aEmail, String aFirstName, String aPassword, String aLastName, int aAccountId, SportPlus aSportPlus)
  {
    email = aEmail;
    firstName = aFirstName;
    password = aPassword;
    lastName = aLastName;
    accountId = aAccountId;
    boolean didAddSportPlus = setSportPlus(aSportPlus);
    if (!didAddSportPlus)
    {
      throw new RuntimeException("Unable to create account due to sportPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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

  public boolean setFirstName(String aFirstName)
  {
    boolean wasSet = false;
    firstName = aFirstName;
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

  public boolean setAccountId(int aAccountId)
  {
    boolean wasSet = false;
    accountId = aAccountId;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public String getPassword()
  {
    return password;
  }

  public String getLastName()
  {
    return lastName;
  }

  public int getAccountId()
  {
    return accountId;
  }
  /* Code from template association_GetOne */
  public SportPlus getSportPlus()
  {
    return sportPlus;
  }
  /* Code from template association_SetOneToMany */
  public boolean setSportPlus(SportPlus aSportPlus)
  {
    boolean wasSet = false;
    if (aSportPlus == null)
    {
      return wasSet;
    }

    SportPlus existingSportPlus = sportPlus;
    sportPlus = aSportPlus;
    if (existingSportPlus != null && !existingSportPlus.equals(aSportPlus))
    {
      existingSportPlus.removeAccount(this);
    }
    sportPlus.addAccount(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    SportPlus placeholderSportPlus = sportPlus;
    this.sportPlus = null;
    if(placeholderSportPlus != null)
    {
      placeholderSportPlus.removeAccount(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "firstName" + ":" + getFirstName()+ "," +
            "password" + ":" + getPassword()+ "," +
            "lastName" + ":" + getLastName()+ "," +
            "accountId" + ":" + getAccountId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "sportPlus = "+(getSportPlus()!=null?Integer.toHexString(System.identityHashCode(getSportPlus())):"null");
  }
}