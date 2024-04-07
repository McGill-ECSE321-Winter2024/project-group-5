package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;

public class ClientResponseDto {

    private int accountId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    // Constructors
    @SuppressWarnings("unused")
    public ClientResponseDto() {
    }

    public ClientResponseDto(Client client) {
        this.accountId = client.getAccountId();
        this.email = client.getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.password = client.getPassword();
    }

    // Getter & Setter AccountId
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    // Getter & Setter Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter & Setter first and last name
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
