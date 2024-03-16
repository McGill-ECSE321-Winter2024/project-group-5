package ca.mcgill.ecse321.SportPlus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.sql.Time;

@Entity
public class Login
{

  @Id
  @GeneratedValue
  private int loginId;
  private Time startTime;
  private Time endTime;

  @ManyToOne // 0..1 is not a tag, if zero or one can be handled in logic
  private Account account;

  protected Login() {
  }

  public Login(Integer aLoginId, Time aStartTime, Time aEndTime, Account aAccount)
  {
    loginId = aLoginId;
    startTime = aStartTime;
    endTime = aEndTime;
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

  public int getLoginId()
  {
    return loginId;
  }
  public Time getStartTime()
  {
    return startTime;
  }

  public Time getEndTime()
  {
    return endTime;
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
}