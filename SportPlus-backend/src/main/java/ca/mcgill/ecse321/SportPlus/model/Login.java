package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Login
{

  @Id
  @GeneratedValue
  private int loginId;

  @ManyToOne
  private Account account;

  protected Login() {
  }

  public Login(int aLoginId, Account aAccount)
  {
    loginId = aLoginId;
    if (!setAccount(aAccount))
    {
      throw new RuntimeException("Unable to create Login due to aAccount");
    }
  }

  public boolean setLoginId(int aLoginId)
  {
    boolean wasSet = false;
    loginId = aLoginId;
    wasSet = true;
    return wasSet;
  }

  public int getLoginId()
  {
    return loginId;
  }
  /* Code from template association_GetOne */
  public Account getAccount()
  {
    return account;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setAccount(Account aNewAccount)
  {
    boolean wasSet = false;
    if (aNewAccount != null)
    {
      account = aNewAccount;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    account = null;
  }
  @Override
  public boolean equals(Object obj){
    if (this == obj) {
      return true;
  }
  if (obj == null || getClass() != obj.getClass()) {
      return false;
  }
  Login other = (Login) obj;
  return loginId == other.loginId &&
         (account == null ? other.account == null : account.equals(other.account));
}
    
  


  public String toString()
  {
    return super.toString() + "["+
            "loginId" + ":" + getLoginId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "account = "+(getAccount()!=null?Integer.toHexString(System.identityHashCode(getAccount())):"null");
  }
}