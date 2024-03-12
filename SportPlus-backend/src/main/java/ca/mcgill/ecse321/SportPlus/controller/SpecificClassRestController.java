package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportPlus.dto.InstructorRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.SpecificClassRequestsDto;
import ca.mcgill.ecse321.SportPlus.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.service.SpecificClassService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@CrossOrigin(origins = "*")
@RestController
public class SpecificClassRestController {


    @Autowired
    private SpecificClassService specificClassService;

    @PostMapping(value = { "/specificclasses/create", "/specificclasses/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificClassResponseDto createSpecificClass(@RequestBody SpecificClassRequestsDto specificclass){
        SpecificClass createdClass = specificClassService.createSpecificClass(
            specificclass.getDate(), 
            specificclass.getStartTime(), 
            specificclass.getEndTime(), 
            specificclass.getInstructorId(), 
            specificclass.getClassTypeId());

            return new SpecificClassResponseDto(createdClass);
    }


    @PostMapping(value = { "/specificclasses/create", "/specificclasses/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificClassResponseDto createRecurrigSpecificClass(@RequestBody SpecificClassRequestsDto specificclass){
        SpecificClass createdClass = specificClassService.createSpecificClass(
            specificclass.getDate(), 
            specificclass.getStartTime(), 
            specificclass.getEndTime(), 
            specificclass.getInstructorId(), 
            specificclass.getClassTypeId());

            return new SpecificClassResponseDto(createdClass);
    }




}
