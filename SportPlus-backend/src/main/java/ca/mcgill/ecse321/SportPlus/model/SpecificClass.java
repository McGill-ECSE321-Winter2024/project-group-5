package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


import java.sql.Date;
import java.sql.Time;

// line 32 "model.ump"
// line 75 "model.ump"
// line 108 "model.ump"
@Entity
public class SpecificClass
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SpecificClass Attributes
  private Date date;
  private Time startTime;
  private Time endTime;

  @Id
  private int classId;

  //SpecificClass Associations
  private Instructor instructor;
  private ClassType classType;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificClass(Date aDate, Time aStartTime, Time aEndTime, int aClassId, Instructor aInstructor, ClassType aClassType)
  {
    date = aDate;
    startTime = aStartTime;
    endTime = aEndTime;
    classId = aClassId;
    if (!setInstructor(aInstructor))
    {
      throw new RuntimeException("Unable to create SpecificClass due to aInstructor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setClassType(aClassType))
    {
      throw new RuntimeException("Unable to create SpecificClass due to aClassType. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartTime(Time aStartTime)
  {
    boolean wasSet = false;
    startTime = aStartTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndTime(Time aEndTime)
  {
    boolean wasSet = false;
    endTime = aEndTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setClassId(int aClassId)
  {
    boolean wasSet = false;
    classId = aClassId;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
  }

  public int getClassId()
  {
    return classId;
  }
  /* Code from template association_GetOne */
  public Instructor getInstructor()
  {
    return instructor;
  }
  /* Code from template association_GetOne */
  public ClassType getClassType()
  {
    return classType;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setInstructor(Instructor aNewInstructor)
  {
    boolean wasSet = false;
    if (aNewInstructor != null)
    {
      instructor = aNewInstructor;
      wasSet = true;
    }
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setClassType(ClassType aNewClassType)
  {
    boolean wasSet = false;
    if (aNewClassType != null)
    {
      classType = aNewClassType;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    instructor = null;
    classType = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "classId" + ":" + getClassId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "instructor = "+(getInstructor()!=null?Integer.toHexString(System.identityHashCode(getInstructor())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "classType = "+(getClassType()!=null?Integer.toHexString(System.identityHashCode(getClassType())):"null");
  }
}