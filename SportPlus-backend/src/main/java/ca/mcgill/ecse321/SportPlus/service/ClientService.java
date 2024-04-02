package ca.mcgill.ecse321.SportPlus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.email.Email;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    // -----------Wrappers-----------//

    @Transactional
    public Client getClient(String email) {
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
        Client client = clientRepository.findByEmail(email);
        return client;
    }

    @Transactional
    public Client getClient(Integer accountId) {
        if (accountId < 0) {
            throw new IllegalArgumentException("Account Id cannot be less than 0!");
        }
        Client client = clientRepository.findByAccountId(accountId);
        return client;
    }

    @Transactional
    public List<Client> getAllClients() {
        return HelperMethods.toList(clientRepository.findAll());
    }

    @Transactional
    public void deleteClient(String email) {
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Person name cannot be empty!");
        }
        clientRepository.deleteByEmail(email);
    }

    // ------------EndWrappers----------//

    @Transactional
    public Client createClient(String sendEmail, String email, String firstName, String password, String lastName) {

        // Input Validation
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
        // Creation of client
        Client client = new Client();
        client.setEmail(email);
        client.setFirstName(firstName);
        client.setPassword(password);
        client.setLastName(lastName);

        // Save the client to the database
        clientRepository.save(client);

        // send verification email to client
        Email.sendEmail(sendEmail, email, "Welcome to SportPlus",
                "Dear " + firstName + ",\nYour account has been created!");

        // Return the client
        return client;
    }

    @Transactional
    public Client updateClientFirstName(String email, String firstName) {

        // Input validation
        if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Client first name cannot be empty!");
        }

        // Creation of client
        client.setFirstName(firstName);

        // Save to teh database
        clientRepository.save(client);

        // Return the client
        return client;
    }

    @Transactional
    public Client updateClientLastName(String email, String lastName) {

        // Input validation
        if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }

        // Create a client
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Client last name cannot be empty!");
        }

        // Set to the name & save & return
        client.setLastName(lastName);
        clientRepository.save(client);
        return client;
    }

    @Transactional
    public Client updateClientPassword(String email, String oldPassword, String password) {

        // Input validation
        if (email == null || HelperMethods.ClientEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }

        // Creation of the client
        Client client = getClient(email);
        if (client == null) {
            throw new IllegalArgumentException("Client with email does not exist!");
        }
        if (!client.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Wrong old password!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid new password!");
        }

        // Set the password & save to DB & return the obj
        client.setPassword(password);
        clientRepository.save(client);
        return client;
    }

}
