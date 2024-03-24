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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import ca.mcgill.ecse321.SportPlus.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.service.RegistrationService;

@CrossOrigin(origins = "*")
@RestController
public class RegistrationRestController {
   
    @Autowired
    private RegistrationService registrationService;

    @GetMapping(value = {"/registrations/getByClient/{email}", "/registrations/getByClient/{email}/"})
    public RegistrationListDto findRegistrationByClient(@PathVariable("email") String theEmail) {
        List<RegistrationResponseDto> registrations = new ArrayList<>();
        for(Registration registration : registrationService.getByClient(theEmail)){
            registrations.add(new RegistrationResponseDto(registration));
        }
        return new RegistrationListDto(registrations);
    }

    @GetMapping(value = {"/registrations/getBySpecificClass/{sessionId}", "/registrations/getBySpecificClass/{sessionId}/"})
    public RegistrationListDto findRegistrationBySpecificClass(@PathVariable("sessionId") int theSessionId) {
        List<RegistrationResponseDto> registrations = new ArrayList<>();
        for(Registration registration : registrationService.getBySpecificClass(theSessionId)){
            registrations.add(new RegistrationResponseDto(registration));
        }
        return new RegistrationListDto(registrations);
    }

    @GetMapping(value = {"/registrations/getByRegistrationId/{regId}", "/registrations/getByRegistrationId/{regId}/"})
    public RegistrationResponseDto findRegistrationByRegId(@PathVariable("regId") int theRegId) {
        Registration registration = registrationService.getByRegistrationId(theRegId);
        return new RegistrationResponseDto(registration);
    }



    @DeleteMapping(value = {"/registrations/deleteByClient/{email}", "/registrations/deleteByClient/{email}/"})
    public void deleteRegistrationByClient(@PathVariable("email") String theEmail) {
        registrationService.deleteByClient(theEmail);
    }

    @DeleteMapping(value = {"/registrations/deleteBySpecificClass/{sessionId}", "/registrations/getBySpecificClass/{sessionId}/"})
    public void deleteRegistrationBySpecificClass(@PathVariable("sessionId") int theSessionId) {
        registrationService.deleteBySpecificClass(theSessionId);
    }

    @DeleteMapping(value = {"/registrations/deleteByRegistrationId/{regId}", "/registrations/getByRegistrationId/{regId}/"})
    public void deleteRegistrationByRegId(@PathVariable("regId") int theRegId) {
        registrationService.deleteByRegistrationId(theRegId);
    }

    @PostMapping(value = {"/registrations/create", "registrations/create/"})
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponseDto createRegistration(@RequestBody RegistrationRequestDto registration){
        Registration createRegistration = registrationService.createRegistration(registration.getSpecificClass().getDate(), registration.getSpecificClass().getStartTime(), registration.getClient().getEmail());
        return new RegistrationResponseDto(createRegistration);
    }
}
