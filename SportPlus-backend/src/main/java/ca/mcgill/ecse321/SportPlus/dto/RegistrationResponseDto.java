package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;



public class RegistrationResponseDto {
    
private int regId;
private SpecificClassResponseDto specificClass;
private ClientResponseDto client;

    @SuppressWarnings("unused")
    public RegistrationResponseDto(){

    }

    public RegistrationResponseDto(Registration registration){
        this.regId = registration.getRegId();
        this.client = new ClientResponseDto(registration.getClient());
        this.specificClass = new SpecificClassResponseDto(registration.getSpecificClass());
    }

    public int getRegId(){
        return regId;
    }

    public void setRegId(int regId){
        this.regId = regId;
    }

    public ClientResponseDto getClient(){
        return client; 
    }

    public void setClient(ClientResponseDto client){
        this.client = client;
    }

    public SpecificClassResponseDto getSpecificClass(){
        return specificClass;
    }

    public void setSpecificClass(SpecificClassResponseDto specificClass){
        this.specificClass = specificClass;
    }



    
}
