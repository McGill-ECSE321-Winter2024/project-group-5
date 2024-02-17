package ca.mcgill.ecse321.SportPlus.model;

import java.util.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.sql.Date;
import java.sql.Time;


@Entity
public class SportPlus
{

  @Id
  private int sportPlusId;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SportPlus Associations
  @OneToMany
  private List<Account> accounts;
  @OneToMany
  private List<Registration> registrations;
  @OneToMany
  private List<SpecificClass> specificClasses;
  @OneToMany
  private List<ClassType> classTypes;
  @OneToMany
  private List<PaymentMethod> paymentMethods;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SportPlus()
  {
    accounts = new ArrayList<Account>();
    registrations = new ArrayList<Registration>();
    specificClasses = new ArrayList<SpecificClass>();
    classTypes = new ArrayList<ClassType>();
    paymentMethods = new ArrayList<PaymentMethod>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Account getAccount(int index)
  {
    Account aAccount = accounts.get(index);
    return aAccount;
  }

  public List<Account> getAccounts()
  {
    List<Account> newAccounts = Collections.unmodifiableList(accounts);
    return newAccounts;
  }

  public int numberOfAccounts()
  {
    int number = accounts.size();
    return number;
  }

  public boolean hasAccounts()
  {
    boolean has = accounts.size() > 0;
    return has;
  }

  public int indexOfAccount(Account aAccount)
  {
    int index = accounts.indexOf(aAccount);
    return index;
  }
  /* Code from template association_GetMany */
  public Registration getRegistration(int index)
  {
    Registration aRegistration = registrations.get(index);
    return aRegistration;
  }

  public List<Registration> getRegistrations()
  {
    List<Registration> newRegistrations = Collections.unmodifiableList(registrations);
    return newRegistrations;
  }

  public int numberOfRegistrations()
  {
    int number = registrations.size();
    return number;
  }

  public boolean hasRegistrations()
  {
    boolean has = registrations.size() > 0;
    return has;
  }

  public int indexOfRegistration(Registration aRegistration)
  {
    int index = registrations.indexOf(aRegistration);
    return index;
  }
  /* Code from template association_GetMany */
  public SpecificClass getSpecificClass(int index)
  {
    SpecificClass aSpecificClass = specificClasses.get(index);
    return aSpecificClass;
  }

  public List<SpecificClass> getSpecificClasses()
  {
    List<SpecificClass> newSpecificClasses = Collections.unmodifiableList(specificClasses);
    return newSpecificClasses;
  }

  public int numberOfSpecificClasses()
  {
    int number = specificClasses.size();
    return number;
  }

  public boolean hasSpecificClasses()
  {
    boolean has = specificClasses.size() > 0;
    return has;
  }

  public int indexOfSpecificClass(SpecificClass aSpecificClass)
  {
    int index = specificClasses.indexOf(aSpecificClass);
    return index;
  }
  /* Code from template association_GetMany */
  public ClassType getClassType(int index)
  {
    ClassType aClassType = classTypes.get(index);
    return aClassType;
  }

  public List<ClassType> getClassTypes()
  {
    List<ClassType> newClassTypes = Collections.unmodifiableList(classTypes);
    return newClassTypes;
  }

  public int numberOfClassTypes()
  {
    int number = classTypes.size();
    return number;
  }

  public boolean hasClassTypes()
  {
    boolean has = classTypes.size() > 0;
    return has;
  }

  public int indexOfClassType(ClassType aClassType)
  {
    int index = classTypes.indexOf(aClassType);
    return index;
  }
  /* Code from template association_GetMany */
  public PaymentMethod getPaymentMethod(int index)
  {
    PaymentMethod aPaymentMethod = paymentMethods.get(index);
    return aPaymentMethod;
  }

  public List<PaymentMethod> getPaymentMethods()
  {
    List<PaymentMethod> newPaymentMethods = Collections.unmodifiableList(paymentMethods);
    return newPaymentMethods;
  }

  public int numberOfPaymentMethods()
  {
    int number = paymentMethods.size();
    return number;
  }

  public boolean hasPaymentMethods()
  {
    boolean has = paymentMethods.size() > 0;
    return has;
  }

  public int indexOfPaymentMethod(PaymentMethod aPaymentMethod)
  {
    int index = paymentMethods.indexOf(aPaymentMethod);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAccounts()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */


  public boolean addAccount(Account aAccount)
  {
    boolean wasAdded = false;
    if (accounts.contains(aAccount)) { return false; }
    SportPlus existingSportPlus = aAccount.getSportPlus();
    boolean isNewSportPlus = existingSportPlus != null && !this.equals(existingSportPlus);
    if (isNewSportPlus)
    {
      aAccount.setSportPlus(this);
    }
    else
    {
      accounts.add(aAccount);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAccount(Account aAccount)
  {
    boolean wasRemoved = false;
    //Unable to remove aAccount, as it must always have a sportPlus
    if (!this.equals(aAccount.getSportPlus()))
    {
      accounts.remove(aAccount);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAccountAt(Account aAccount, int index)
  {  
    boolean wasAdded = false;
    if(addAccount(aAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAccounts()) { index = numberOfAccounts() - 1; }
      accounts.remove(aAccount);
      accounts.add(index, aAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAccountAt(Account aAccount, int index)
  {
    boolean wasAdded = false;
    if(accounts.contains(aAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAccounts()) { index = numberOfAccounts() - 1; }
      accounts.remove(aAccount);
      accounts.add(index, aAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAccountAt(aAccount, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRegistrations()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Registration addRegistration(int aRegId, SpecificClass aSpecificClass, Client aClient)
  {
    return new Registration(aRegId, this, aSpecificClass, aClient);
  }

  public boolean addRegistration(Registration aRegistration)
  {
    boolean wasAdded = false;
    if (registrations.contains(aRegistration)) { return false; }
    SportPlus existingSportPlus = aRegistration.getSportPlus();
    boolean isNewSportPlus = existingSportPlus != null && !this.equals(existingSportPlus);
    if (isNewSportPlus)
    {
      aRegistration.setSportPlus(this);
    }
    else
    {
      registrations.add(aRegistration);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRegistration(Registration aRegistration)
  {
    boolean wasRemoved = false;
    //Unable to remove aRegistration, as it must always have a sportPlus
    if (!this.equals(aRegistration.getSportPlus()))
    {
      registrations.remove(aRegistration);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addRegistrationAt(Registration aRegistration, int index)
  {  
    boolean wasAdded = false;
    if(addRegistration(aRegistration))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegistrations()) { index = numberOfRegistrations() - 1; }
      registrations.remove(aRegistration);
      registrations.add(index, aRegistration);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRegistrationAt(Registration aRegistration, int index)
  {
    boolean wasAdded = false;
    if(registrations.contains(aRegistration))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfRegistrations()) { index = numberOfRegistrations() - 1; }
      registrations.remove(aRegistration);
      registrations.add(index, aRegistration);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addRegistrationAt(aRegistration, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSpecificClasses()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public SpecificClass addSpecificClass(Date aDate, Time aStartTime, Time aEndTime, int aSessionId, ClassType aClassType)
  {
    return new SpecificClass(aDate, aStartTime, aEndTime, aSessionId, this, aClassType);
  }

  public boolean addSpecificClass(SpecificClass aSpecificClass)
  {
    boolean wasAdded = false;
    if (specificClasses.contains(aSpecificClass)) { return false; }
    SportPlus existingSportPlus = aSpecificClass.getSportPlus();
    boolean isNewSportPlus = existingSportPlus != null && !this.equals(existingSportPlus);
    if (isNewSportPlus)
    {
      aSpecificClass.setSportPlus(this);
    }
    else
    {
      specificClasses.add(aSpecificClass);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSpecificClass(SpecificClass aSpecificClass)
  {
    boolean wasRemoved = false;
    //Unable to remove aSpecificClass, as it must always have a sportPlus
    if (!this.equals(aSpecificClass.getSportPlus()))
    {
      specificClasses.remove(aSpecificClass);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSpecificClassAt(SpecificClass aSpecificClass, int index)
  {  
    boolean wasAdded = false;
    if(addSpecificClass(aSpecificClass))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecificClasses()) { index = numberOfSpecificClasses() - 1; }
      specificClasses.remove(aSpecificClass);
      specificClasses.add(index, aSpecificClass);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSpecificClassAt(SpecificClass aSpecificClass, int index)
  {
    boolean wasAdded = false;
    if(specificClasses.contains(aSpecificClass))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSpecificClasses()) { index = numberOfSpecificClasses() - 1; }
      specificClasses.remove(aSpecificClass);
      specificClasses.add(index, aSpecificClass);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSpecificClassAt(aSpecificClass, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfClassTypes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public ClassType addClassType(String aName, String aDescription, int aTypeId, boolean aApproved, Owner aApprover)
  {
    return new ClassType(aName, aDescription, aTypeId, aApproved, this, aApprover);
  }

  public boolean addClassType(ClassType aClassType)
  {
    boolean wasAdded = false;
    if (classTypes.contains(aClassType)) { return false; }
    SportPlus existingSportPlus = aClassType.getSportPlus();
    boolean isNewSportPlus = existingSportPlus != null && !this.equals(existingSportPlus);
    if (isNewSportPlus)
    {
      aClassType.setSportPlus(this);
    }
    else
    {
      classTypes.add(aClassType);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeClassType(ClassType aClassType)
  {
    boolean wasRemoved = false;
    //Unable to remove aClassType, as it must always have a sportPlus
    if (!this.equals(aClassType.getSportPlus()))
    {
      classTypes.remove(aClassType);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addClassTypeAt(ClassType aClassType, int index)
  {  
    boolean wasAdded = false;
    if(addClassType(aClassType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClassTypes()) { index = numberOfClassTypes() - 1; }
      classTypes.remove(aClassType);
      classTypes.add(index, aClassType);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveClassTypeAt(ClassType aClassType, int index)
  {
    boolean wasAdded = false;
    if(classTypes.contains(aClassType))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfClassTypes()) { index = numberOfClassTypes() - 1; }
      classTypes.remove(aClassType);
      classTypes.add(index, aClassType);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addClassTypeAt(aClassType, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfPaymentMethods()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PaymentMethod addPaymentMethod(String aCardNumber, Date aExpDate, String aCvc, String aCardHolderName, Client aClient)
  {
    return new PaymentMethod(aCardNumber, aExpDate, aCvc, aCardHolderName, this, aClient);
  }

  public boolean addPaymentMethod(PaymentMethod aPaymentMethod)
  {
    boolean wasAdded = false;
    if (paymentMethods.contains(aPaymentMethod)) { return false; }
    SportPlus existingSportPlus = aPaymentMethod.getSportPlus();
    boolean isNewSportPlus = existingSportPlus != null && !this.equals(existingSportPlus);
    if (isNewSportPlus)
    {
      aPaymentMethod.setSportPlus(this);
    }
    else
    {
      paymentMethods.add(aPaymentMethod);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removePaymentMethod(PaymentMethod aPaymentMethod)
  {
    boolean wasRemoved = false;
    //Unable to remove aPaymentMethod, as it must always have a sportPlus
    if (!this.equals(aPaymentMethod.getSportPlus()))
    {
      paymentMethods.remove(aPaymentMethod);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addPaymentMethodAt(PaymentMethod aPaymentMethod, int index)
  {  
    boolean wasAdded = false;
    if(addPaymentMethod(aPaymentMethod))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentMethods()) { index = numberOfPaymentMethods() - 1; }
      paymentMethods.remove(aPaymentMethod);
      paymentMethods.add(index, aPaymentMethod);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMovePaymentMethodAt(PaymentMethod aPaymentMethod, int index)
  {
    boolean wasAdded = false;
    if(paymentMethods.contains(aPaymentMethod))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfPaymentMethods()) { index = numberOfPaymentMethods() - 1; }
      paymentMethods.remove(aPaymentMethod);
      paymentMethods.add(index, aPaymentMethod);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addPaymentMethodAt(aPaymentMethod, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (accounts.size() > 0)
    {
      Account aAccount = accounts.get(accounts.size() - 1);
      aAccount.delete();
      accounts.remove(aAccount);
    }
    
    while (registrations.size() > 0)
    {
      Registration aRegistration = registrations.get(registrations.size() - 1);
      aRegistration.delete();
      registrations.remove(aRegistration);
    }
    
    while (specificClasses.size() > 0)
    {
      SpecificClass aSpecificClass = specificClasses.get(specificClasses.size() - 1);
      aSpecificClass.delete();
      specificClasses.remove(aSpecificClass);
    }
    
    while (classTypes.size() > 0)
    {
      ClassType aClassType = classTypes.get(classTypes.size() - 1);
      aClassType.delete();
      classTypes.remove(aClassType);
    }
    
    while (paymentMethods.size() > 0)
    {
      PaymentMethod aPaymentMethod = paymentMethods.get(paymentMethods.size() - 1);
      aPaymentMethod.delete();
      paymentMethods.remove(aPaymentMethod);
    }
    
  }

}