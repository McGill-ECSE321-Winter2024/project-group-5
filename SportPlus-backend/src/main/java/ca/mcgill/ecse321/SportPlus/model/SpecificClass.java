package ca.mcgill.ecse321.SportPlus.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class SpecificClass
{
  @Id
  @GeneratedValue
  private int sessionId;
  private Date date;
  private Time startTime;
  private Time endTime;


  //SpecificClass Associations
  private SportPlus sportPlus;
  @ManyToOne
  private Instructor supervisor;
  @ManyToOne
  private ClassType classType;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SpecificClass(Date aDate, Time aStartTime, Time aEndTime, int aSessionId, SportPlus aSportPlus, ClassType aClassType)
  {
    date = aDate;
    startTime = aStartTime;
    endTime = aEndTime;
    sessionId = aSessionId;
    boolean didAddSportPlus = setSportPlus(aSportPlus);
    if (!didAddSportPlus)
    {
      throw new RuntimeException("Unable to create specificClass due to sportPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
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

  public boolean setSessionId(int aSessionId)
  {
    boolean wasSet = false;
    sessionId = aSessionId;
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

  public int getSessionId()
  {
    return sessionId;
  }
  /* Code from template association_GetOne */
  public SportPlus getSportPlus()
  {
    return sportPlus;
  }
  /* Code from template association_GetOne */
  public Instructor getSupervisor()
  {
    return supervisor;
  }

  public boolean hasSupervisor()
  {
    boolean has = supervisor != null;
    return has;
  }
  /* Code from template association_GetOne */
  public ClassType getClassType()
  {
    return classType;
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
      existingSportPlus.removeSpecificClass(this);
    }
    sportPlus.addSpecificClass(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setSupervisor(Instructor aNewSupervisor)
  {
    boolean wasSet = false;
    supervisor = aNewSupervisor;
    wasSet = true;
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
    SportPlus placeholderSportPlus = sportPlus;
    this.sportPlus = null;
    if(placeholderSportPlus != null)
    {
      placeholderSportPlus.removeSpecificClass(this);
    }
    supervisor = null;
    classType = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "sessionId" + ":" + getSessionId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "startTime" + "=" + (getStartTime() != null ? !getStartTime().equals(this)  ? getStartTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endTime" + "=" + (getEndTime() != null ? !getEndTime().equals(this)  ? getEndTime().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "sportPlus = "+(getSportPlus()!=null?Integer.toHexString(System.identityHashCode(getSportPlus())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "supervisor = "+(getSupervisor()!=null?Integer.toHexString(System.identityHashCode(getSupervisor())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "classType = "+(getClassType()!=null?Integer.toHexString(System.identityHashCode(getClassType())):"null");
  }
}