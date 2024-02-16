package ca.mcgill.ecse321.SportPlus.model;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.sql.Date;

// line 36 "model.ump"
// line 123 "model.ump"
@Entity
public class PaymentMethod
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentMethod Attributes
  @Id
  private String cardNumber;
  private Date expdate;
  private int cvc;

  //PaymentMethod Associations
  @ManyToOne
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentMethod(String aCardNumber, Date aExpdate, int aCvc, Client aClient)
  {
    cardNumber = aCardNumber;
    expdate = aExpdate;
    cvc = aCvc;
    if (!setClient(aClient))
    {
      throw new RuntimeException("Unable to create PaymentMethod due to aClient. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setCardNumber(String aCardNumber)
  {
    boolean wasSet = false;
    cardNumber = aCardNumber;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpdate(Date aExpdate)
  {
    boolean wasSet = false;
    expdate = aExpdate;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvc(int aCvc)
  {
    boolean wasSet = false;
    cvc = aCvc;
    wasSet = true;
    return wasSet;
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public Date getExpdate()
  {
    return expdate;
  }

  public int getCvc()
  {
    return cvc;
  }
  /* Code from template association_GetOne */
  public Client getClient()
  {
    return client;
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
    client = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cvc" + ":" + getCvc()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "expdate" + "=" + (getExpdate() != null ? !getExpdate().equals(this)  ? getExpdate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}