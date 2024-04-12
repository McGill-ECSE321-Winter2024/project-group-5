package ca.mcgill.ecse321.SportPlus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.SportPlus.dto.SpecificClassRequestsDto;
import ca.mcgill.ecse321.SportPlus.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.service.SpecificClassService;
import ca.mcgill.ecse321.SportPlus.dto.RecurringSpecificClassRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.RecurringSpecificClassesResponseDto;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/specificClass")
public class SpecificClassRestController {

    @Autowired
    private SpecificClassService specificClassService;

    @PostMapping(value = { "/create", "/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificClassResponseDto createSpecificClass(@RequestBody SpecificClassRequestsDto specificclass) {

        // Set the attributes of the specificClass
        SpecificClass createdClass = specificClassService.createSpecificClass(
                specificclass.getDate(),
                specificclass.getStartTime(),
                specificclass.getEndTime(),
                specificclass.getInstructorId(),
                specificclass.getClassTypeId());
        return new SpecificClassResponseDto(createdClass);
    }

    @PostMapping(value = { "/recurring", "/recurring/" })
    @ResponseStatus(HttpStatus.CREATED)
    public RecurringSpecificClassesResponseDto createRecurrigSpecificClasses(
            @RequestBody RecurringSpecificClassRequestDto recurringSpecificCLassRequest) {

        List<SpecificClass> recurringClasses = specificClassService.createRecurringSpecificClasses(
                recurringSpecificCLassRequest.getDate(), recurringSpecificCLassRequest.getEndDate(),
                recurringSpecificCLassRequest.getStartTime(),
                recurringSpecificCLassRequest.getEndTime(), recurringSpecificCLassRequest.getDayOfWeek(),
                recurringSpecificCLassRequest.getInstructorId(),
                recurringSpecificCLassRequest.getClassTypeId());

        // Create an empty list of SpecificClassResponseDto
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        // Manually iterate through each SpecificClass in the recurringClasses list
        for (SpecificClass specificClass : recurringClasses) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        // Wrap the list of SpecificClassResponseDto in
        // RecurringSpecificClassesResponseDto
        return new RecurringSpecificClassesResponseDto(responseDtos);

    }

    @PutMapping(value = { "/{id}/date", "/{id}/date/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassDate(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {
        // Update the Date with ID
        SpecificClass updatedClass = specificClassService.updateDateSpecificClass(id,
                requestsDtoSpecifcClass.getDate());

        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/time", "/{id}/time/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassTime(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {
        // update specificClass time using the id with the srvice method
        SpecificClass updatedClass = specificClassService.updateTimeSpecificClass(id,
                requestsDtoSpecifcClass.getStartTime(), requestsDtoSpecifcClass.getEndTime());
        // Return SpecificClassResponseDto
        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/classtype", "/{id}/classtype/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassClassType(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {
        // update specificClass time classType the id with the srvice method
        SpecificClass updatedClass = specificClassService.updateClassTypeSpecificClass(id,
                requestsDtoSpecifcClass.getClassTypeId());

        // Return SpecificClassResponseDto
        return new SpecificClassResponseDto(updatedClass);
    }

    // assignes an instructor to a specific class
    @PutMapping(value = { "/{id}/assign-instructor", "/{id}/assign-instructor/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto assignInstructorSpecificClass(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {

        SpecificClass updatedClass = specificClassService.assignInstructorSpecificClass(id,
                requestsDtoSpecifcClass.getInstructorId());

        return new SpecificClassResponseDto(updatedClass);
    }

    // unassigns an instructor from a specific class
    @PutMapping(value = { "/{id}/remove-instructor", "/{id}/remove-instructor/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto removeInstructorSpecificClass(@PathVariable("id") int id) {

        SpecificClass updatedClass = specificClassService.removeInstructorSpecificClass(id);

        return new SpecificClassResponseDto(updatedClass);
    }

    // gets all classes instructor is assigned to in a list
    @GetMapping(value = { "/instructor/{instructorId}", "/instructor/{instructorId}/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByInstructor(@PathVariable("instructorId") int instructorId) {

        List<SpecificClass> classes = specificClassService.getByInstructor(instructorId);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping(value = { "/by-date", "/by-date/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByDate(
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date utilDate) {

        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        List<SpecificClass> classes = specificClassService.getByDate(sqlDate);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping(value = { "/class-type/{classTypeId}", "/class-type/{classTypeId}/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByClassType(@PathVariable("classTypeId") int classTypeId) {

        // Get SpecificClasses by ClassType
        List<SpecificClass> classes = specificClassService.getByClassType(classTypeId);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        // Convert the SpecifcCLasses list to SpecificClassResponseDto list
        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping(value = { "/by-date-range", "/by-date-range/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate) {

        // Convert to SQL time and Date
        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndtDate = new java.sql.Date(endDate.getTime());

        // Get a list of SpicificClasses by DateRange
        List<SpecificClass> classes = specificClassService.getByDateRange(sqlStartDate, sqlEndtDate);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        // Convert the SpecifcCLasses list to SpecificClassResponseDto list
        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping(value = { "/available", "available/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getAvailableSpecificClasses() {

        // Get a list of available SpicificClasses
        List<SpecificClass> availableClasses = specificClassService.getAvailable();
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : availableClasses) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping(value = { "/by-date-and-start-time", "/by-date-and-start-time/" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto getSpecificClassByDateAndTime(
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date date,
            @RequestParam(name = "startTime") String startTime) {

        // Convert to SQL time and Date
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        Time startTim = Time.valueOf(startTime);

        // Get the specificClass by date and start time
        SpecificClass session = specificClassService.getByDateAndStartTime(sqlDate, startTim);

        return new SpecificClassResponseDto(session);
    }

    @GetMapping(value = { "/all", "all/" })
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getAllSpecificClasses() {

        // Get all SpecificClasses
        List<SpecificClass> allClasses = specificClassService.getAll();
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        // Convert the SpecifcCLasses list to SpecificClassResponseDto list
        for (SpecificClass specificClass : allClasses) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @DeleteMapping(value = { "/class-type/{classTypeId}", "/class-type/{classTypeId}/" })
    public ResponseEntity<Void> deleteSpecificClassesByClassType(@PathVariable("classTypeId") int classTypeId) {

        // Delete all specificClasses with a certain ClassType
        specificClassService.deleteByClassType(classTypeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = { "/deleteBySessionId/{sessionId}", "/deleteBySessionId/{sessionId}/" })
    public ResponseEntity<Void> deleteSpecificClassesBySessionId(@PathVariable("sessionId") int sessionId) {

        // Delete all specificClasses with a certain ClassType
        specificClassService.deleteByClassType(sessionId);
        return ResponseEntity.noContent().build();
    }

}
