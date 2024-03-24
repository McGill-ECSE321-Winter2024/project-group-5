package ca.mcgill.ecse321.SportPlus.dto;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

public class RegistrationRequestDto {
    private SpecificClass specificClass;
    private Client client;

    public RegistrationRequestDto() {
    }

    public RegistrationRequestDto(SpecificClass specificClass, Client client) {
        this.client = client;
        this.specificClass = specificClass;
    }

    public SpecificClass getSpecificClass() {
        return specificClass;
    }

    public void setSpecificClass(SpecificClass specificClass) {
        this.specificClass = specificClass;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
