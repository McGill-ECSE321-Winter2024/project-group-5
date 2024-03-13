package ca.mcgill.ecse321.SportPlus.dto;

import java.util.List;

public class ClientListDto {

    private List<ClientResponseDto> clients;

    public ClientListDto() {
    }

    public ClientListDto(List<ClientResponseDto> clients) {
        this.clients = clients;
    }

    public List<ClientResponseDto> getClients() {
        return clients;
    }

    public void setClients(List<ClientResponseDto> clients) {
        this.clients = clients;
    }

}
