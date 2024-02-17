package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Registration
{

  @Id
  @GeneratedValue
  private int regId;

  //Registration Associations
  private SportPlus sportPlus;
  private SpecificClass specificClass;
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Registration(int aRegId, SportPlus aSportPlus, SpecificClass aSpecificClass, Client aClient)
  {
    regId = aRegId;
    boolean didAddSportPlus = setSportPlus(aSportPlus);
    if (!didAddSportPlus)
    {
      throw new RuntimeException("Unable to create registration due to sportPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setSpecificClass(aSpecificClass))
    {
      throw new RuntimeException("Unable to create Registration due to aSpecificClass. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setClient(aClient))
    {
      throw new RuntimeException("Unable to create Registration due to aClient. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRegId(int aRegId)
  {
    boolean wasSet = false;
    regId = aRegId;
    wasSet = true;
    return wasSet;
  }

  public int getRegId()
  {
    return regId;
  }
  /* Code from template association_GetOne */
  public SportPlus getSportPlus()
  {
    return sportPlus;
  }
  /* Code from template association_GetOne */
  public SpecificClass getSpecificClass()
  {
    return specificClass;
  }
  /* Code from template association_GetOne */
  public Client getClient()
  {
    return client;
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
      existingSportPlus.removeRegistration(this);
    }
    sportPlus.addRegistration(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setSpecificClass(SpecificClass aNewSpecificClass)
  {
    boolean wasSet = false;
    if (aNewSpecificClass != null)
    {
      specificClass = aNewSpecificClass;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setClient(Client aNewClient)
  {
    boolean wasSet = false;
    if (aNewClient != null)
    {
      client = aNewClient;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    SportPlus placeholderSportPlus = sportPlus;
    this.sportPlus = null;
    if(placeholderSportPlus != null)
    {
      placeholderSportPlus.removeRegistration(this);
    }
    specificClass = null;
    client = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "regId" + ":" + getRegId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "sportPlus = "+(getSportPlus()!=null?Integer.toHexString(System.identityHashCode(getSportPlus())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "specificClass = "+(getSpecificClass()!=null?Integer.toHexString(System.identityHashCode(getSpecificClass())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}