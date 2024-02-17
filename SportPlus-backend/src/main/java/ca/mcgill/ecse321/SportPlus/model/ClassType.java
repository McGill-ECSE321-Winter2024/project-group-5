package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/



// line 24 "model.ump"
// line 80 "model.ump"
// line 111 "model.ump"
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int typeId;
  private boolean approved;

  //ClassType Associations
  @ManyToOne
  @JoinColumn(name = "approver_id")
  private Owner approver;

  //------------------------
  // CONSTRUCTOR
  //------------------------
  protected ClassType() {

  }

  public ClassType(String aName, String aDescription, int aTypeId, boolean aApproved, Owner aApprover)
  {
    name = aName;
    description = aDescription;
    typeId = aTypeId;
    approved = aApproved;
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

  public boolean setApproved(boolean aApproved)
  {
    boolean wasSet = false;
    approved = aApproved;
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

  public boolean getApproved()
  {
    return approved;
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
            "typeId" + ":" + getTypeId()+ "," +
            "approved" + ":" + getApproved()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "approver = "+(getApprover()!=null?Integer.toHexString(System.identityHashCode(getApprover())):"null");
  }
}