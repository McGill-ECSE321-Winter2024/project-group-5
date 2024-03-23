package ca.mcgill.ecse321.SportPlus.dto;

import java.sql.Time;

public class LoginRequestDto {

    private String type;
    private String email;
    private String password;
    private Time currentTime;

    @SuppressWarnings("unused")
    public LoginRequestDto() {
    }

    public LoginRequestDto(String type, String email, String password, Time currentTime) {
        this.type = type;
        this.email = email;
        this.password = password;
        this.currentTime = currentTime;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Time getCurrentTime() {
        return currentTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrentTime(Time currentTime) {
        this.currentTime = currentTime;
    }

}
