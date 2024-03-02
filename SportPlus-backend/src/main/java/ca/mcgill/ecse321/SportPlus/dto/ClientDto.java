package ca.mcgill.ecse321.SportPlus.dto;

public class ClientDto {

    //private int accountId;
    private String email;
    private String firstName;
    private String password;
    private String lastName;

    public ClientDto() {

    }
    public ClientDto(String email){
        this.email = email;
    }
    public ClientDto(String email, String firstName, String lastName, String password){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setPasword(String password){
        this.password = password;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    
}
