package ca.mcgill.ecse321.SportPlus.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;


import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;
import ca.mcgill.ecse321.SportPlus.service.ClientService;


@Service
public class RegistrationService {

    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SpecificClassRepository specificClassRepository;



    //-----------Wrappers-----------//


    //Check for if client exist
    //check for specific class exists
    //get get by instructors ?
    
    @Transactional
    public List<Registration> getByClient(String email){
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
        Client client = clientRepository.findByEmail(email);
        List<Registration> registration = registrationRepository.findByClient(client);
        return registration;
    }

    @Transactional
    public List<Registration> getBySpecificClass(Integer sessionId){
        if (sessionId < 0) {
            throw new IllegalArgumentException("Session Id cannot be less than 0!");
        }
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        List<Registration> registration = registrationRepository.findBySpecificClass(specificClass);
        return registration;
    }

    @Transactional
    public Registration getByRegistrationId(Integer regId){
        if (regId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }
        Registration registration = registrationRepository.findByRegId(regId);
        return registration;
    }



    @Transactional
    public void deleteByClient(String email){
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Client email cannot be empty!");
        }
        Client client = clientRepository.findByEmail(email);
        registrationRepository.deleteByClient(client);
    }

    @Transactional
    public void deleteBySpecificClass(Integer sessionId){
        if (sessionId < 0) {
            throw new IllegalArgumentException("Session Id cannot be less than 0!");
        }
        SpecificClass specificClass = specificClassRepository.findBySessionId(sessionId);
        registrationRepository.deleteBySpecificClass(specificClass);
    }

    @Transactional
    public void deleteByRegistrationId(Integer regId){
        if (regId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }
        registrationRepository.deleteById(regId);
    }

     //------------EndWrappers----------//

     @Transactional
     public Registration createRegistration(Integer regId, Integer specificClassId, String clientEmail){
        if (clientEmail == null || HelperMethods.ClientEmailCheck(clientEmail).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        if (regId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }
        if (specificClassId < 0) {
            throw new IllegalArgumentException("Registration Id cannot be less than 0!");
        }

        SpecificClass specificClass = specificClassRepository.findBySessionId(specificClassId);
        Client client = clientRepository.findByEmail(clientEmail);

        Registration registration = new Registration(regId, specificClass, client);
        return registration;
     }




    
}
