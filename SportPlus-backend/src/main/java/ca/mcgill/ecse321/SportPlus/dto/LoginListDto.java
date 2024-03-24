package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class LoginListDto {
    private List<LoginResponseDto> logins;

    public LoginListDto() {
    }

    public LoginListDto(List<LoginResponseDto> logins) {
        this.logins = logins;
    }

    public List<LoginResponseDto> getLogins() {
        return logins;
    }

    public void setLogins(List<LoginResponseDto> logins) {
        this.logins = logins;
    }

}
