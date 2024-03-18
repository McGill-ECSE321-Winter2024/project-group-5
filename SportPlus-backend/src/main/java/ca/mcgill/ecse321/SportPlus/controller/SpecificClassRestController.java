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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

@CrossOrigin(origins = "*")
@RestController
public class SpecificClassRestController {

    @Autowired
    private SpecificClassService specificClassService;

    @PostMapping(value = { "/specificclasses/create", "/specificclasses/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public SpecificClassResponseDto createSpecificClass(@RequestBody SpecificClassRequestsDto specificclass) {

        SpecificClass createdClass = specificClassService.createSpecificClass(
                specificclass.getDate(),
                specificclass.getStartTime(),
                specificclass.getEndTime(),
                specificclass.getInstructorId(),
                specificclass.getClassTypeId());
        return new SpecificClassResponseDto(createdClass);
    }

    @PostMapping(value = { "/recurring" })
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

    @PutMapping(value = { "/{id}/date" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassDate(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {
        SpecificClass updatedClass = specificClassService.updateDateSpecificClass(id,
                requestsDtoSpecifcClass.getDate());

        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/time" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassTime(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {

        SpecificClass updatedClass = specificClassService.updateTimeSpecificClass(id,
                requestsDtoSpecifcClass.getStartTime(), requestsDtoSpecifcClass.getEndTime());

        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/classtype" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto updateSpecificClassClassType(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {

        SpecificClass updatedClass = specificClassService.updateClassTypeSpecificClass(id,
                requestsDtoSpecifcClass.getClassTypeId());

        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/assign-instructor" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto assignInstructorSpecificClass(@PathVariable("id") int id,
            @RequestBody SpecificClassRequestsDto requestsDtoSpecifcClass) {

        SpecificClass updatedClass = specificClassService.assignInstructorSpecificClass(id,
                requestsDtoSpecifcClass.getInstructorId());

        return new SpecificClassResponseDto(updatedClass);
    }

    @PutMapping(value = { "/{id}/remove-instructor" })
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto removeInstructorSpecificClass(@PathVariable("id") int id) {

        SpecificClass updatedClass = specificClassService.removeInstructorSpecificClass(id);

        return new SpecificClassResponseDto(updatedClass);
    }

    @GetMapping("/instructor/{instructorId}")
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

    @GetMapping("/by-date")
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

    @GetMapping("/class-type/{classTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByClassType(@PathVariable("classTypeId") int classTypeId) {

        List<SpecificClass> classes = specificClassService.getByClassType(classTypeId);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping("/by-date-range")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getSpecificClassByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate) {

        java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEndtDate = new java.sql.Date(endDate.getTime());

        List<SpecificClass> classes = specificClassService.getByDateRange(sqlStartDate, sqlEndtDate);
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : classes) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getAvailableSpecificClasses() {

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

    @GetMapping("/by-date-and-start-time")
    @ResponseStatus(HttpStatus.OK)
    public SpecificClassResponseDto getSpecificClassByDateAndTime(
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date date,
            @RequestParam(name = "startTime") String startTime) {

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        Time startTim = Time.valueOf(startTime);

        SpecificClass session = specificClassService.getByDateAndStartTime(sqlDate, startTim);

        return new SpecificClassResponseDto(session);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecificClassResponseDto> getAllSpecificClasses() {

        List<SpecificClass> allClasses = specificClassService.getAll();
        List<SpecificClassResponseDto> responseDtos = new ArrayList<>();

        for (SpecificClass specificClass : allClasses) {
            // Convert each SpecificClass to SpecificClassResponseDto
            SpecificClassResponseDto responseDto = new SpecificClassResponseDto(specificClass);

            // Add the converted SpecificClassResponseDto to the responseDtos list
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }

    @DeleteMapping("/class-type/{classTypeId}")
    public ResponseEntity<Void> deleteSpecificClassesByClassType(@PathVariable("classTypeId") int classTypeId) {
        specificClassService.deleteByClassType(classTypeId);
        return ResponseEntity.noContent().build();
    }

}
