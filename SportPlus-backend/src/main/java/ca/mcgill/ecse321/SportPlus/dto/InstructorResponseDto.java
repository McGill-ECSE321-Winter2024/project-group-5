package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Instructor;

public class InstructorResponseDto {

    private int accountId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    @SuppressWarnings("unused")
    public InstructorResponseDto() {
    }

    public InstructorResponseDto(Instructor instructor) {
        this.accountId = instructor.getAccountId();
        this.email = instructor.getEmail();
        this.firstName = instructor.getFirstName();
        this.lastName = instructor.getLastName();
        this.password = instructor.getPassword();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

}
