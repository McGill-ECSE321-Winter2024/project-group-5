package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Owner;

public class OwnerResponseDto {

    private int accountId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    // Constructors
    @SuppressWarnings("unused")
    public OwnerResponseDto() {
    }

    public OwnerResponseDto(Owner owner) {
        this.accountId = owner.getAccountId();
        this.email = owner.getEmail();
        this.firstName = owner.getFirstName();
        this.lastName = owner.getLastName();
        this.password = owner.getPassword();
    }

    // Getter and setter accountId
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    // Getter and setter email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and seyyer first & last name
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
