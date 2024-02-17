package ca.mcgill.ecse321.SportPlus.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class PaymentMethod
{
  @Id
  private int paymentId;

  private String cardNumber;
  private Date expDate;
  private String cvc;
  private String cardHolderName;

  //PaymentMethod Associations
  @ManyToOne
  private SportPlus sportPlus;
  @ManyToOne
  private Client client;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PaymentMethod(String aCardNumber, Date aExpDate, String aCvc, String aCardHolderName, SportPlus aSportPlus, Client aClient)
  {
    cardNumber = aCardNumber;
    expDate = aExpDate;
    cvc = aCvc;
    cardHolderName = aCardHolderName;
    boolean didAddSportPlus = setSportPlus(aSportPlus);
    if (!didAddSportPlus)
    {
      throw new RuntimeException("Unable to create paymentMethod due to sportPlus. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
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
  /* Code from template association_GetOne */
  public SportPlus getSportPlus()
  {
    return sportPlus;
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
      existingSportPlus.removePaymentMethod(this);
    }
    sportPlus.addPaymentMethod(this);
    wasSet = true;
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
      placeholderSportPlus.removePaymentMethod(this);
    }
    client = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "cardNumber" + ":" + getCardNumber()+ "," +
            "cvc" + ":" + getCvc()+ "," +
            "cardHolderName" + ":" + getCardHolderName()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "expDate" + "=" + (getExpDate() != null ? !getExpDate().equals(this)  ? getExpDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "sportPlus = "+(getSportPlus()!=null?Integer.toHexString(System.identityHashCode(getSportPlus())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "client = "+(getClient()!=null?Integer.toHexString(System.identityHashCode(getClient())):"null");
  }
}