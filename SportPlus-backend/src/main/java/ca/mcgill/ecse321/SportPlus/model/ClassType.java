package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class ClassType {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ClassType Attributes
  @Id
  @GeneratedValue
  private int typeId;
  private String name;
  private String description;
  private boolean approved;

  // ClassType Associations
  @ManyToOne
  @JoinColumn(name = "approver_account_id")
  private Owner approver;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public ClassType() {
  }

  public ClassType(String aName, String aDescription, int aTypeId, boolean aApproved, Owner aApprover) {
    name = aName;
    description = aDescription;
    typeId = aTypeId;
    approved = aApproved;
    approver = aApprover;
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

  public Owner getApprover() {
    return approver;
  }

  public boolean setApprover(Owner aNewApprover) {
    boolean wasSet = false;
    if (aNewApprover != null) {
      approver = aNewApprover;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    approver = null;
  }

  public String toString() {
    return super.toString() + "[" +
        "name" + ":" + getName() + "," +
        "description" + ":" + getDescription() + "," +
        "typeId" + ":" + getTypeId() + "," +
        "approved" + ":" + getApproved() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "approver = "
        + (getApprover() != null ? Integer.toHexString(System.identityHashCode(getApprover())) : "null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    ClassType classType = (ClassType) obj;
    return typeId == classType.typeId &&
        approved == classType.approved &&
        Objects.equals(name, classType.name) &&
        Objects.equals(description, classType.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, typeId, approved);
  }
}