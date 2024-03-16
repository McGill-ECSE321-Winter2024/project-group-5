package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Time;

public class LoginRequestDto {

    public enum AccountType{
        OWNER,
        CLIENT, 
        INSTRUCTOR,
    }

    private int loginId;
    private String accountEmail;
    private Time currentTime;
    private AccountType accountType;

    @SuppressWarnings("unused")
    public LoginRequestDto(){
    }

    public LoginRequestDto(int aLoginId, String email,Time aStartTime, AccountType type ){
        loginId = aLoginId;
        accountEmail = email;
        currentTime = aStartTime;
  
        accountType = type;
    
    }
    public void setStartTime(Time time){
        currentTime = time;
    }
    public void setLoginId(int id){
        loginId = id;
    }
    public void setAccountType(AccountType type){
        accountType = type;
    }
    public AccountType getAccountType(){
        return accountType;
    }
    public int getLoginId(){
        return loginId;
    }
    public Time getCurrentTime(){
        return currentTime;
    }
    public String getAccountEmail(){
        return accountEmail;
    }
    public void setAccountEmail(String email){
        accountEmail = email; 
    }
}