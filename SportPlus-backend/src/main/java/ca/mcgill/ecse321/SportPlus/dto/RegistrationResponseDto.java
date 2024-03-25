package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Registration;

// Data transfer object representing a registration response
public class RegistrationResponseDto {

    private int regId; // Registration ID
    private SpecificClassResponseDto specificClass; // Response DTO for the specific class associated with the registration
    private ClientResponseDto client; // Response DTO for the client associated with the registration

    // Default constructor (unused)
    @SuppressWarnings("unused")
    public RegistrationResponseDto() {
    }

    // Constructor initializing the registration response DTO based on a registration entity
    public RegistrationResponseDto(Registration registration) {
        this.regId = registration.getRegId();
        this.client = new ClientResponseDto(registration.getClient());
        this.specificClass = new SpecificClassResponseDto(registration.getSpecificClass());
    }

    // Getter for the registration ID
    public int getRegId() {
        return regId;
    }

    // Setter for the registration ID
    public void setRegId(int regId) {
        this.regId = regId;
    }

    // Getter for the client response DTO
    public ClientResponseDto getClient() {
        return client;
    }

    // Setter for the client response DTO
    public void setClient(ClientResponseDto client) {
        this.client = client;
    }

    // Getter for the specific class response DTO
    public SpecificClassResponseDto getSpecificClass() {
        return specificClass;
    }

    // Setter for the specific class response DTO
    public void setSpecificClass(SpecificClassResponseDto specificClass) {
        this.specificClass = specificClass;
    }

}
