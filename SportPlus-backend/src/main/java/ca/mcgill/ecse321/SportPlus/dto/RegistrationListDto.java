package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class RegistrationListDto {

    private List<RegistrationResponseDto> registrations;

    public RegistrationListDto(){

    }

    public RegistrationListDto(List<RegistrationResponseDto> registrations){
        this.registrations = registrations;
    }
    
    public List<RegistrationResponseDto> getRegistrations(){
        return registrations;
    }

    public void setRegistrations(List<RegistrationResponseDto> registrations){
        this.registrations = registrations;
    }
}
