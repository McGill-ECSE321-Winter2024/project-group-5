package ca.mcgill.ecse321.SportPlus.dto;
import ca.mcgill.ecse321.SportPlus.model.Login;

public class LoginResponseDto {

    private int loginId;
    private String accountEmail;
    private String accountType;

    public LoginResponseDto() {
    }

    public LoginResponseDto(Login login, String type) {
        loginId = login.getLoginId();
        accountEmail = login.getAccount().getEmail();
        accountType = type;
    }

    public void setLoginId(int id) {
        loginId = id;
    }

    public void setAccountType(String type) {
        accountType = type;
    }

    public String getAccountType() {
        return accountType;
    }

    public int getLoginId() {
        return loginId;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String email) {
        accountEmail = email;
    }
}
