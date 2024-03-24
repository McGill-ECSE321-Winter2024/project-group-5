package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

public class RegistrationRequestDto {
    private SpecificClass specificClass;
    private Client client;
    private int regId;

    public RegistrationRequestDto(SpecificClass specificClass, Client client,int regId ){
        this.client = client;
        this.regId = regId;
        this.specificClass = specificClass;

    }

    public int getRegId(){
        return regId;
    }

    public void setRegId(int regId){
        this.regId = regId;
    }
    
    public SpecificClass getSpecificClass(){
        return specificClass;
    }

    
    public void setSpecificClass(SpecificClass specificClass){
        this.specificClass = specificClass;
    }

    public Client getClient(){
        return client;
    }

    public void setClient(Client client){
        this.client = client;
    }

}
