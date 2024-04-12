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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.service.RegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class RegistrationRestController {

    @Autowired
    private RegistrationService registrationService;

    // Endpoint to retrieve registrations by client email
    @GetMapping(value = { "/registrations/getByClient/{email}", "/registrations/getByClient/{email}/" })
    public RegistrationListDto findRegistrationByClient(@PathVariable("email") String theEmail) {
        List<RegistrationResponseDto> registrations = new ArrayList<>();
        // Iterate through registrations obtained from the service layer
        for (Registration registration : registrationService.getByClient(theEmail)) {
            // Convert each registration to DTO
            registrations.add(new RegistrationResponseDto(registration));
        }
        // Return DTO list
        return new RegistrationListDto(registrations);
    }

    // Endpoint to retrieve registrations by specific class session ID 
    @GetMapping(value = { "/registrations/getBySpecificClass/{sessionId}",
            "/registrations/getBySpecificClass/{sessionId}/" })
    public RegistrationListDto findRegistrationBySpecificClass(@PathVariable("sessionId") int theSessionId) {
        List<RegistrationResponseDto> registrations = new ArrayList<>();
        // Iterate through registrations obtained from the service layer
        for (Registration registration : registrationService.getBySpecificClass(theSessionId)) {
            // Convert each registration to DTO
            registrations.add(new RegistrationResponseDto(registration));
        }
        // Return DTO list
        return new RegistrationListDto(registrations);
    }

    // Endpoint to retrieve registration by registration ID
    @GetMapping(value = { "/registrations/getByRegistrationId/{regId}", "/registrations/getByRegistrationId/{regId}/" })
    public RegistrationResponseDto findRegistrationByRegId(@PathVariable("regId") int theRegId) {
        Registration registration = registrationService.getByRegistrationId(theRegId);
        if(registration == null){
            return null;
        }
        // Convert registration to DTO and return
        return new RegistrationResponseDto(registration);
    }

    // Endpoint to delete registrations by client email
    @DeleteMapping(value = { "/registrations/deleteByClient/{email}", "/registrations/deleteByClient/{email}/" })
    public void deleteRegistrationByClient(@PathVariable("email") String theEmail) {
        registrationService.deleteByClient(theEmail);
    }

    // Endpoint to delete registrations by specific class session ID
    @DeleteMapping(value = { "/registrations/deleteBySpecificClass/{sessionId}",
            "/registrations/deleteBySpecificClass/{sessionId}/" })
    public void deleteRegistrationBySpecificClass(@PathVariable("sessionId") int theSessionId) {
        registrationService.deleteBySpecificClass(theSessionId);
    }

    // Endpoint to delete registrations by registration ID
    @DeleteMapping(value = { "/registrations/deleteByRegistrationId/{regId}",
            "/registrations/deleteByRegistrationId/{regId}/" })
    public void deleteRegistrationByRegId(@PathVariable("regId") int theRegId) {
        registrationService.deleteByRegistrationId(theRegId);
    }

    // Endpoint to create a new registration
    @PostMapping(value = { "/registrations/create/{specificClassId}/{clientEmail}", "registrations/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto createRegistration(@PathVariable("clientEmail") String email,@PathVariable("specificClassId") int specificClassId) {
        // Create registration using service layer
        Registration createRegistration = registrationService.createRegistration("send", specificClassId, email);
        // Convert created registration to DTO and return
        return new RegistrationResponseDto(createRegistration);
    }

}
