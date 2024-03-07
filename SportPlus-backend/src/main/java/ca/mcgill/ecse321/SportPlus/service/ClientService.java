package ca.mcgill.ecse321.SportPlus.service;

import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import jakarta.transaction.Transactional;


@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    //-----------Wrappers-----------//

    @Transactional
	public Client getClient(String email) {
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
		Client client = clientRepository.findByEmail(email);
		return client;
	}

    @Transactional
    public Client getClient(Integer accountId){
        if (accountId < 0) {
            throw new IllegalArgumentException("Account Id cannot be less than 0!");
        }
        Client client = clientRepository.findByAccountId(accountId);
        return client;

    }
    @Transactional
    public void deleteClient(String email){
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Person name cannot be empty!");
        }
        clientRepository.deleteByEmail(email);
    }

    @Transactional
    public  List<Client> getAllClients(){
        return clientRepository.findAll();
    }
    //------------EndWrappers----------//

    //-----------Helper--------------//
    @Transactional
    public boolean clientWithEmailExists(String email){
        Client client = getClient(email);
        if (client == null) {
            return false;
        }
        return true;
    }
    //-----------EndHelper----------//

    @Transactional
	public Client createClient(String email, String password, String firstName, String lastName) {
		Client client = new Client();
        if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Client first name cannot be empty!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Client last name cannot be empty!");
        }
		client.setEmail(email);
        client.setPassword(password);
        client.setFirstName(firstName);
        client.setLastName(lastName);
		clientRepository.save(client);
		return client;
	}

    @Transactional
    public Client updateClientFirstName(String email, String firstName){
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Client first name cannot be empty!");
        }
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        client.setFirstName(firstName);
        clientRepository.save(client);
        return client;
    }

    @Transactional
    public Client updateClientLastName(String email, String lastName){
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Client first name cannot be empty!");
        }
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        client.setLastName(lastName);
        clientRepository.save(client);
        return client;
    }
    @Transactional
    public Client updateClientPassword(String email, String password){
         if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid password!");
        }
        client.setPassword(password);
        clientRepository.save(client);
        return client; 
    }

    @Transactional
    public Client updateClientEmail(int accountId, String email){
        if (accountId < 0) {
            throw new IllegalArgumentException("Account Id cannot be less than 0!");
        }
        Client client = getClient(accountId);
        if (client == null) {
            throw new IllegalArgumentException("Client with account Id does not exist!");
        }
        if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        if(clientWithEmailExists(email)){
            throw new IllegalArgumentException("Email already in use");
        }
        client.setEmail(email);
        clientRepository.save(client);
        return client;
    }
    
}
