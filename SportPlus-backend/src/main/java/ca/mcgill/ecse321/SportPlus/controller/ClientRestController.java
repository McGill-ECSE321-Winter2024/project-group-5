package ca.mcgill.ecse321.SportPlus.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.ClientListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.service.ClientService;

@CrossOrigin(origins = "*")
@RestController
public class ClientRestController {
    
    @Autowired
    private ClientService clientService;

    @GetMapping(value = { "/clients/getByEmail/{email}", "/clients/getByEmail/{email}/" })
    public ClientResponseDto findClientByEmail(@PathVariable("email") String theEmail) {
        Client client = clientService.getClient(theEmail);
        return new ClientResponseDto(client);
    }

    @GetMapping(value = { "/clients/getById/{accountId}", "/clients/getById/{accountId}/" })
    public ClientResponseDto findClientByAccountId(@PathVariable("accountId") int theId) {
        Client client = clientService.getClient(theId);
        return new ClientResponseDto(client);
    }

    @GetMapping(value = { "/clients/all", "/clients/all/" })
    public ClientListDto getAllClients() {
        List<ClientResponseDto> clients = new ArrayList<>();
        for (Client client : clientService.getAllClients()) {
            clients.add(new ClientResponseDto(client));
        }
        return new ClientListDto(clients);
    }

    @DeleteMapping(value = { "/clients/delete/{email}", "/clients/delete/{email}/" })
    public void deleteClientByEmail(@PathVariable("email") String theEmail) {
        clientService.deleteClient(theEmail);
    }

    @PostMapping(value = { "/clients/create", "/clients/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponseDto createClient(@RequestBody ClientRequestDto client) {
        Client createdClient = clientService.createClient(client.getEmail(), client.getFirstName(), client.getPassword(), client.getLastName());
        return new ClientResponseDto(createdClient);
    }

    @PutMapping(value = { "/clients/updateFirstName/{email}/{newFirstName}", "/clients/updateFirstName/{email}/{newFirstName}/" })
    public ClientResponseDto updateClientFirstName(@PathVariable("email") String theEmail, @PathVariable("newFirstName") String theFirstName) {
        Client client = clientService.getClient(theEmail);
        clientService.updateClientFirstName(theEmail, theFirstName);
        client = clientService.getClient(theEmail);
        return new ClientResponseDto(client);
    }

    @PutMapping(value = { "/clients/updateLastName/{email}/{newLastName}", "/clients/updateLastName/{email}/{newLastName}/" })
    public ClientResponseDto updateClientLastName(@PathVariable("email") String theEmail, @PathVariable("newLastName") String theLastName) {
        Client client = clientService.getClient(theEmail);
        clientService.updateClientLastName(theEmail, theLastName);
        client = clientService.getClient(theEmail);
        return new ClientResponseDto(client);
    }

    @PutMapping(value = { "/clients/updatePassword/{email}/{oldPassword}/{newPassword}", "/clients/updatePassword/{email}/{oldPassword}/{newPassword}/" })
    public ClientResponseDto updateClientPassword(@PathVariable("email") String theEmail, @PathVariable("oldPassword") String theOldPassword, @PathVariable("newPassword") String thePassword) {
        Client client = clientService.getClient(theEmail);
        clientService.updateClientPassword(theEmail, theOldPassword, thePassword);
        client = clientService.getClient(theEmail);
        return new ClientResponseDto(client);
    }

}
