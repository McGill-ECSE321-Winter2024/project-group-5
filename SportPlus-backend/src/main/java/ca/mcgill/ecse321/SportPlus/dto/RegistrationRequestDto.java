package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

// Data transfer object representing a registration request
public class RegistrationRequestDto {
    
    private SpecificClass specificClass; // Specific class for the registration
    private Client client; // Client making the registration

    // Default constructor
    public RegistrationRequestDto() {
    }

    // Constructor initializing the specific class and client
    public RegistrationRequestDto(SpecificClass specificClass, Client client) {
        this.client = client;
        this.specificClass = specificClass;
    }

    // Getter for the specific class
    public SpecificClass getSpecificClass() {
        return specificClass;
    }

    // Setter for the specific class
    public void setSpecificClass(SpecificClass specificClass) {
        this.specificClass = specificClass;
    }

    // Getter for the client
    public Client getClient() {
        return client;
    }

    // Setter for the client
    public void setClient(Client client) {
        this.client = client;
    }

}
