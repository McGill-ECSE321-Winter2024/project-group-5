package ca.mcgill.ecse321.SportPlus.dto;

public class OwnerRequestDto {

    private String firstName;
    private String lastName;
    private String password;

    // Constructors
    public OwnerRequestDto() {
    }

    public OwnerRequestDto(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }


    // Getters and settes first & last name 
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

    // Getter and setter password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
