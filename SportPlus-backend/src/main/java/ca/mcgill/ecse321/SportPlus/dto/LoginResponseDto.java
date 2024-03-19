package ca.mcgill.ecse321.SportPlus.dto;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto.AccountType;
import ca.mcgill.ecse321.SportPlus.model.Login;

public class LoginResponseDto {

    private int loginId;
    private String accountEmail;
    private AccountType accountType;

    public LoginResponseDto() {
    }

    // public LoginResponseDto(int aLoginId, String email, AccountType type) {
    //     loginId = aLoginId;
    //     accountEmail = email;
    //     accountType = type;
    // }

    public LoginResponseDto(Login login, AccountType type) {
        loginId = login.getLoginId();
        accountEmail = login.getAccount().getEmail();
        accountType = type;
    }

    public void setLoginId(int id) {
        loginId = id;
    }

    public void setAccountType(AccountType type) {
        accountType = type;
    }

    public AccountType getAccountType() {
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
