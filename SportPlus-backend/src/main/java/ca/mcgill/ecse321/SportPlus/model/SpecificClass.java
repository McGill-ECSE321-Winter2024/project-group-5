package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import java.sql.Date;
import java.sql.Time;

@Entity
public class SpecificClass {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // SpecificClass Attributes
  @Id
  @GeneratedValue
  private int sessionId;
  private String name;
  private Date date;
  private Time startTime;
  private Time endTime;

  // SpecificClass Associations
  @ManyToOne
  private Instructor supervisor;
  @ManyToOne
  @JoinColumn(name = "class_type_type_id")
  private ClassType classType;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  protected SpecificClass() {
  }

  public SpecificClass(Date aDate, Time start1, Time aEndTime, int aSessionId, ClassType aClassType, String aName) {
    date = aDate;
    startTime = start1;
    endTime = aEndTime;
    sessionId = aSessionId;
    name = aName;
    if (!setClassType(aClassType)) {
      throw new RuntimeException(
          "Unable to create SpecificClass due to aClassType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setDate(Date aDate) {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime) {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime) {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setSessionId(int aSessionId) {
    boolean wasSet = false;
    sessionId = aSessionId;
    wasSet = true;
    return wasSet;
  }

  public Date getDate() {
    return date;
  }

  public String getName() {
    return name;
  }

  public Time getStartTime() {
    return startTime;
  }

  public Time getEndTime() {
    return endTime;
  }

  public int getSessionId() {
    return sessionId;
  }

  public Instructor getSupervisor() {
    return supervisor;
  }

  public boolean hasSupervisor() {
    boolean has = supervisor != null;
    return has;
  }

  public ClassType getClassType() {
    return classType;
  }

  public boolean setSupervisor(Instructor aNewSupervisor) {
    boolean wasSet = false;
    supervisor = aNewSupervisor;
    wasSet = true;
    return wasSet;
  }

  public boolean setClassType(ClassType aNewClassType) {
    boolean wasSet = false;
    if (aNewClassType != null) {
      classType = aNewClassType;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete() {
    supervisor = null;
    classType = null;
  }

  public String toString() {
    return super.toString() + "[" +
        "sessionId" + ":" + getSessionId() + "]" + System.getProperties().getProperty("line.separator") +
        "  " + "date" + "="
        + (getDate() != null ? !getDate().equals(this) ? getDate().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "startTime" + "="
        + (getStartTime() != null
            ? !getStartTime().equals(this) ? getStartTime().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "endTime" + "="
        + (getEndTime() != null ? !getEndTime().equals(this) ? getEndTime().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "supervisor = "
        + (getSupervisor() != null ? Integer.toHexString(System.identityHashCode(getSupervisor())) : "null")
        + System.getProperties().getProperty("line.separator") +
        "  " + "classType = "
        + (getClassType() != null ? Integer.toHexString(System.identityHashCode(getClassType())) : "null");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    SpecificClass specificClass = (SpecificClass) obj;
    return Objects.equals(date, specificClass.date) &&
        Objects.equals(startTime, specificClass.startTime) &&
        Objects.equals(endTime, specificClass.endTime) &&
        sessionId == specificClass.sessionId &&
        Objects.equals(classType, specificClass.classType) &&
        Objects.equals(supervisor, specificClass.supervisor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, startTime, endTime, sessionId, classType, supervisor);
  }
}