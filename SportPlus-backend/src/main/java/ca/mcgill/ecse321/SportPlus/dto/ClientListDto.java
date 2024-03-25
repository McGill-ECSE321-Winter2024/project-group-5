package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class ClientListDto {

    private List<ClientResponseDto> clients;

    // Constructors
    public ClientListDto() {
    }

    public ClientListDto(List<ClientResponseDto> clients) {
        this.clients = clients;
    }

    // Getter
    public List<ClientResponseDto> getClients() {
        return clients;
    }

    // Setter
    public void setClients(List<ClientResponseDto> clients) {
        this.clients = clients;
    }

}
