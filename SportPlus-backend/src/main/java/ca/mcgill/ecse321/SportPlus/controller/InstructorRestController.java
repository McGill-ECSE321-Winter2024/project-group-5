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

import ca.mcgill.ecse321.SportPlus.dto.InstructorListDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.service.InstructorService;

@CrossOrigin(origins = "*")
@RestController
public class InstructorRestController {
    
    @Autowired
    private InstructorService instructorService;

    @GetMapping(value = { "/instructors/getByEmail/{email}", "/instructors/getByEmail/{email}/" })
    public InstructorResponseDto findInstructorByEmail(@PathVariable("email") String theEmail) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/getById/{accountId}", "/instructors/getById/{accountId}/" })
    public InstructorResponseDto findInstructorByAccountId(@PathVariable("accountId") int theId) {
        Instructor instructor = instructorService.getInstructor(theId);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/all", "/instructors/all/" })
    public InstructorListDto getAllInstructors() {
        List<InstructorResponseDto> instructors = new ArrayList<>();
        for (Instructor instructor : instructorService.getAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructors);
    }

    @DeleteMapping(value = { "/instructors/delete/{email}", "/instructors/delete/{email}/" })
    public void deleteInstructorByEmail(@PathVariable("email") String theEmail) {
        instructorService.deleteInstructor(theEmail);
    }

    @PostMapping(value = { "/instructors/create", "/instructors/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorRequestDto createInstructor(@RequestBody InstructorRequestDto instructor) {
        Instructor createdInstructor = instructorService.createInstructor(instructor.getEmail(), instructor.getFirstName(), instructor.getPassword(), instructor.getLastName());
        return new InstructorRequestDto(createdInstructor.getEmail(), createdInstructor.getFirstName(), createdInstructor.getLastName(), createdInstructor.getPassword());
    }

    @PutMapping(value = { "/instructors/updateEmail/{accountId}/{newEmail}", "/instructors/updateEmail/{accountId}/{newEmail}/" })
    public InstructorRequestDto updateInstructorEmail(@PathVariable("newEmail") String theEmail, @PathVariable("accountId") int theId) {
        Instructor instructor = instructorService.getInstructor(theId);
        instructorService.updateInstructorEmail(theId, theEmail);
        instructor = instructorService.getInstructor(theId);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/updateFirstName/{email}/{newFirstName}", "/instructors/updateFirstName/{email}/{newFirstName}/" })
    public InstructorRequestDto updateInstructorFirstName(@PathVariable("email") String theEmail, @PathVariable("newFirstName") String theFirstName) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorFirstName(theEmail, theFirstName);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/updateLastName/{email}/{newLastName}", "/instructors/updateLastName/{email}/{newLastName}/" })
    public InstructorRequestDto updateInstructorLastName(@PathVariable("email") String theEmail, @PathVariable("newLastName") String theLastName) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorLastName(theEmail, theLastName);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/updatePassword/{email}/{newPassword}", "/instructors/updatePassword/{email}/{newPassword}/" })
    public InstructorRequestDto updateInstructorPassword(@PathVariable("email") String theEmail, @PathVariable("newPassword") String thePassword) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorPassword(theEmail, thePassword);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

}
