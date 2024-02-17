package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ClassType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int typeId;

  private String name;
  private String description;
  private boolean approved;

  // ClassType Associations
  @ManyToOne
  private SportPlus sportPlus;

  @ManyToOne
  @JoinColumn(name = "approver_id")
  private Owner approver;

  protected ClassType() {

  }

  public ClassType(String aName, String aDescription, int aTypeId, boolean aApproved, SportPlus aSportPlus,
      Owner aApprover) {
    name = aName;
    description = aDescription;
    typeId = aTypeId;
    approved = aApproved;
    boolean didAddSportPlus = setSportPlus(aSportPlus);
    if (!didAddSportPlus) {
      throw new RuntimeException(
          "Unable to create classType due to sportPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setApprover(aApprover)) {
      throw new RuntimeException(
          "Unable to create ClassType due to aApprover. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setDescription(String aDescription) {
    boolean wasSet = false;
    description = aDescription;
    wasSet = true;
    return wasSet;
  }

  public boolean setTypeId(int aTypeId) {
    boolean wasSet = false;
    typeId = aTypeId;
    wasSet = true;
    return wasSet;
  }

  public boolean setApproved(boolean aApproved) {
    boolean wasSet = false;
    approved = aApproved;
    wasSet = true;
    return wasSet;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public int getTypeId() {
    return typeId;
  }

  public boolean getApproved() {
    return approved;
  }

  /* Code from template association_GetOne */
  public SportPlus getSportPlus() {
    return sportPlus;
  }

  /* Code from template association_GetOne */
  public Owner getApprover() {
    return approver;
  }

  /* Code from template association_SetOneToMany */
  public boolean setSportPlus(SportPlus aSportPlus) {
    boolean wasSet = false;
    if (aSportPlus == null) {
      return wasSet;
    }

    SportPlus existingSportPlus = sportPlus;
    sportPlus = aSportPlus;
    if (existingSportPlus != null && !existingSportPlus.equals(aSportPlus)) {
      existingSportPlus.removeClassType(this);
    }
    sportPlus.addClassType(this);
    wasSet = true;
    return wasSet;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setApprover(Owner aNewApprover) {
    boolean wasSet = false;
    if (aNewApprover != null) {
      approver = aNewApprover;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    SportPlus placeholderSportPlus = sportPlus;
    this.sportPlus = null;
    if (placeholderSportPlus != null) {
      placeholderSportPlus.removeClassType(this);
    }
    approver = null;
  }

  public String toString() {
    return super.toString() + "[" +
        "name" + ":" + getName() + "," +
        "description" + ":" + getDescription() + "," +
        "typeId" + ":" + getTypeId() + "," +
        "approved" + ":" + getApproved() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "sportPlus = "
        + (getSportPlus() != null ? Integer.toHexString(System.identityHashCode(getSportPlus())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "approver = "
        + (getApprover() != null ? Integer.toHexString(System.identityHashCode(getApprover())) : "null");
  }
}