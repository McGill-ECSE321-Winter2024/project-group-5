package ca.mcgill.ecse321.SportPlus.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.33.0.6934.a386b0a58 modeling language!*/


import java.sql.Date;

// line 51 "model.ump"
// line 120 "model.ump"
// line 128 "model.ump"
@Entity
public class PaymentMethod
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PaymentMethod Attributes
  private String cardNumber;
  private Date expDate;
  private String cvc;
  private String cardHolderName;
  @Id
  private int cardId;

  //PaymentMethod Associations
  @ManyToOne
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentMethod(String aCardNumber, Date aExpDate, String aCvc, String aCardHolderName, int aCardId, Client aClient)
  {
    cardNumber = aCardNumber;
    expDate = aExpDate;
    cvc = aCvc;
    cardHolderName = aCardHolderName;
    cardId = aCardId;
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

  public boolean setExpDate(Date aExpDate)
  {
    boolean wasSet = false;
    expDate = aExpDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setCvc(String aCvc)
  {
    boolean wasSet = false;
    cvc = aCvc;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardHolderName(String aCardHolderName)
  {
    boolean wasSet = false;
    cardHolderName = aCardHolderName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCardId(int aCardId)
  {
    boolean wasSet = false;
    cardId = aCardId;
    wasSet = true;
    return wasSet;
  }

  public String getCardNumber()
  {
    return cardNumber;
  }

  public Date getExpDate()
  {
    return expDate;
  }

  public String getCvc()
  {
    return cvc;
  }

  public String getCardHolderName()
  {
    return cardHolderName;
  }

  public int getCardId()
  {
    return cardId;
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
            "cvc" + ":" + getCvc()+ "," +
            "cardHolderName" + ":" + getCardHolderName()+ "," +
            "cardId" + ":" + getCardId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "expDate" + "=" + (getExpDate() != null ? !getExpDate().equals(this)  ? getExpDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}