package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;



// line 26 "model.ump"
// line 79 "model.ump"
// line 112 "model.ump"
@Entity
public class ClassType
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //ClassType Attributes
  private String name;
  private String description;

  @Id
  private int typeId;

  @ManyToOne
  //ClassType Associations
  private Owner approver;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public ClassType(String aName, String aDescription, int aTypeId, Owner aApprover)
  {
    name = aName;
    description = aDescription;
    typeId = aTypeId;
    if (!setApprover(aApprover))
    {
      throw new RuntimeException("Unable to create ClassType due to aApprover. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription)
  {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setTypeId(int aTypeId)
  {
    boolean wasSet = false;
    typeId = aTypeId;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public int getTypeId()
  {
    return typeId;
  }
  /* Code from template association_GetOne */
  public Owner getApprover()
  {
    return approver;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setApprover(Owner aNewApprover)
  {
    boolean wasSet = false;
    if (aNewApprover != null)
    {
      approver = aNewApprover;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    approver = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "description" + ":" + getDescription()+ "," +
            "typeId" + ":" + getTypeId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "approver = "+(getApprover()!=null?Integer.toHexString(System.identityHashCode(getApprover())):"null");
  }
}