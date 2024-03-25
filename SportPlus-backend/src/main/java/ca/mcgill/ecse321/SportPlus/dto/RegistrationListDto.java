package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

// Data transfer object representing a list of registrations
public class RegistrationListDto {

    // List of registration response DTOs
    private List<RegistrationResponseDto> registrations;

    // Default constructor
    public RegistrationListDto() {

    }

    // Constructor initializing the list of registrations
    public RegistrationListDto(List<RegistrationResponseDto> registrations) {
        this.registrations = registrations;
    }

    // Getter for the list of registrations
    public List<RegistrationResponseDto> getRegistrations() {
        return registrations;
    }

    // Setter for the list of registrations
    public void setRegistrations(List<RegistrationResponseDto> registrations) {
        this.registrations = registrations;
    }
}
