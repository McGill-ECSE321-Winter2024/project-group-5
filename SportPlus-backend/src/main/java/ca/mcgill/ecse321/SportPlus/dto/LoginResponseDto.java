package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Login;

public class LoginResponseDto {

    private int loginId;
    private String accountEmail;
    private String accountType;

    @SuppressWarnings("unused")
    public LoginResponseDto() {
    }

    public LoginResponseDto(Login login, String accountType) {
        if (login == null) {
            throw new IllegalArgumentException("Login cannot be null");
        }
        this.loginId = login.getLoginId();
        this.accountEmail = login.getAccount().getEmail();
        this.accountType = accountType;
    }

    public int getLoginId() {
        return loginId;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
