package ca.mcgill.ecse321.SportPlus.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.email.Email;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

// Service class for managing registrations
@Service
public class RegistrationService {

    // Autowired repositories for database operations
    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    SpecificClassRepository specificClassRepository;

    @Autowired
    InstructorRepository instructorRepository;

    // -----------Wrappers-----------//

    // Method to retrieve registrations by client email
    @Transactional
    public List<Registration> getByClient(String email) {
        // Check if the email is null or empty
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
        // Find the client by email
        Client client = clientRepository.findByEmail(email);
        // Retrieve registrations associated with the client
        List<Registration> registration = registrationRepository.findByClient(client);
        return registration;
    }

    // Method to retrieve registrations by specific class session ID
    @Transactional
    public List<Registration> getBySpecificClass(Integer sessionId) {
        // Check if the session ID is valid
        if (sessionId < 0) {
            throw new IllegalArgumentException("Session Id cannot be less than 0!");
        }
        // Find the specific class by session ID
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        // Retrieve registrations associated with the specific class
        List<Registration> registration = registrationRepository.findBySpecificClass(specificClass);
        return registration;
    }

    // Method to retrieve registration by registration ID
    @Transactional
    public Registration getByRegistrationId(Integer regId) {
        // Check if the registration ID is valid
        if (regId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }
        // Find the registration by ID
        Registration registration = registrationRepository.findByRegId(regId);

        return registration;
    }

    // Method to delete registrations by client email
    @Transactional
    public void deleteByClient(String email) {
        // Check if the email is null or empty
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
        // Find the client by email
        Client client = clientRepository.findByEmail(email);
        // Delete registrations associated with the client
        registrationRepository.deleteByClient(client);
    }

    // Method to delete registrations by specific class session ID
    @Transactional
    public void deleteBySpecificClass(Integer sessionId) {
        // Check if the session ID is valid
        if (sessionId < 0) {
            throw new IllegalArgumentException("Session Id cannot be less than 0!");
        }
        // Find the specific class by session ID
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        // Delete registrations associated with the specific class
        registrationRepository.deleteBySpecificClass(specificClass);
    }

    // Method to delete registrations by registration ID
    @Transactional
    public void deleteByRegistrationId(Integer regId) {
        // Check if the registration ID is valid
        if (regId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }
        // Delete registration by ID
        registrationRepository.deleteByRegId(regId);
    }

    // ------------EndWrappers----------//

    // Method to create a new registration
    @Transactional
    public Registration createRegistration(String sendEmail, String specificClassName, String clientEmail) {
        // Check if the client email is valid
        if (clientEmail == null || HelperMethods.ClientEmailCheck(clientEmail).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }

        // Find the specific class by name
        SpecificClass specificClass = specificClassRepository.findByName(specificClassName);
        // Find the client by email
        Client client = clientRepository.findByEmail(clientEmail);

        // Create a new registration
        Registration registration = new Registration(0, specificClass, client);
        // Save the registration
        registrationRepository.save(registration);

        // send verification email to client
        Email.sendEmail(sendEmail, clientEmail, "Registration Confirmed", "Dear " + client.getFirstName()
                + ",\nYou are registered to " + specificClass.getClassType().getName() + "!");

        // return the registration
        return registration;
    }

}
