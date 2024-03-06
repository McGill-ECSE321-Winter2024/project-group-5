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

    @GetMapping(value = { "/instructors/email/{specificEmail}", "/instructors/email/{specificEmail}/" })
    public InstructorResponseDto findInstructorByEmail(@PathVariable("specificEmail") String theEmail) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/accountId/{specificId}", "/instructors/accountId/{specificId}/" })
    public InstructorResponseDto findInstructorByAccoutnId(@PathVariable("specificId") int theId) {
        Instructor instructor = instructorService.getInstructor(theId);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors", "instructors/" })
    public InstructorListDto getAllInstructors() {
        List<InstructorResponseDto> instructors = new ArrayList<>();
        for (Instructor instructor : instructorService.getAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructors);
    }

    @DeleteMapping(value = { "/instructors/email/{specificEmail}", "/instructors/email/{specificEmail}/" })
    public void deleteInstructor(@PathVariable("specificEmail") String theEmail) {
        instructorService.deleteInstructor(theEmail);
    }

    @PostMapping(value = { "/instructors", "/instructors/" })
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorRequestDto createInstructor(@RequestBody InstructorRequestDto instructor) {
        Instructor createdInstructor = instructorService.createInstructor(instructor.getEmail(), instructor.getFirstName(), instructor.getPassword(), instructor.getLastName());
        return new InstructorRequestDto(createdInstructor.getEmail(), createdInstructor.getFirstName(), createdInstructor.getLastName(), createdInstructor.getPassword());
    }

    @PutMapping(value = { "/instructors/email/{accountId}/{specificEmail}", "/instructors/email/{accountId}/{specificEmail}/" })
    public InstructorRequestDto updateInstructorEmail(@PathVariable("specificEmail") String theEmail, @PathVariable("accountId") int theId) {
        Instructor instructor = instructorService.getInstructor(theId);
        instructorService.updateInstructorEmail(theId, theEmail);
        instructor = instructorService.getInstructor(theId);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/firstName/{specificEmail}/{specificFirstName}", "/instructors/firstName/{specificEmail}/{specificFirstName}/" })
    public InstructorRequestDto updateInstructorFirstName(@PathVariable("specificEmail") String theEmail, @PathVariable("specificFirstName") String theFirstName) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorFirstName(theEmail, theFirstName);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/lastName/{specificEmail}/{specificLastName}", "/instructors/lastName/{specificEmail}/{specificLastName}/" })
    public InstructorRequestDto updateInstructorLastName(@PathVariable("specificEmail") String theEmail, @PathVariable("specificLastName") String theLastName) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorLastName(theEmail, theLastName);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping(value = { "/instructors/password/{specificEmail}/{specificPassword}", "/instructors/lastName/{specificEmail}/{specificPassword}/" })
    public InstructorRequestDto updateInstructorPassword(@PathVariable("specificEmail") String theEmail, @PathVariable("specificPassword") String thePassword) {
        Instructor instructor = instructorService.getInstructor(theEmail);
        instructorService.updateInstructorPassword(theEmail, thePassword);
        instructor = instructorService.getInstructor(theEmail);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

}
