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

    @GetMapping(value = { "/instructors/{email}", "/instructors/{email}/" })
    public InstructorResponseDto findInstructorByEmail(@PathVariable String email) {
        Instructor instructor = instructorService.getInstructor(email);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/{accountId}", "/instructors/{accountId}/" })
    public InstructorResponseDto findInstructorByAccountId(@PathVariable int accountId) {
        Instructor instructor = instructorService.getInstructor(accountId);
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors", "/instructors/"})
    public InstructorListDto getAllInstructors() {
        List<InstructorResponseDto> instructors = new ArrayList<InstructorResponseDto>();
        for (Instructor instructor : instructorService.getAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }
        return new InstructorListDto(instructors);
    }

    @DeleteMapping(value = { "/instructors/{email}", "/instructors/{email}/" })
    public void deleteInstructor(@PathVariable String email) {
        instructorService.deleteInstructor(email);
    }

    @PostMapping(value = { "/instructors", "/instructors/"})
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorRequestDto createInstructor(@RequestBody InstructorRequestDto instructor) {
        Instructor createdInstructor = instructorService.createInstructor(instructor.getEmail(), instructor.getFirstName(), instructor.getPassword(), instructor.getLastName());
        return new InstructorRequestDto(createdInstructor.getEmail(), createdInstructor.getFirstName(), createdInstructor.getLastName(), createdInstructor.getPassword());
    }

    @PutMapping("/instructors/{accountId}/email")
    public InstructorRequestDto updateInstructorEmail(@PathVariable int accountId, @RequestBody String email) {
        Instructor instructor = instructorService.getInstructor(accountId);
        instructorService.updateInstructorEmail(accountId, email);
        instructor = instructorService.getInstructor(accountId);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping("/instructors/{email}/firstName")
    public InstructorRequestDto updateInstructorFirstName(@PathVariable String email, @RequestBody String firstName) {
        Instructor instructor = instructorService.getInstructor(email);
        instructorService.updateInstructorFirstName(email, firstName);
        instructor = instructorService.getInstructor(email);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping("/instructors/{email}/lastName")
    public InstructorRequestDto updateInstructorLastName(@PathVariable String email, @RequestBody String lastName) {
        Instructor instructor = instructorService.getInstructor(email);
        instructorService.updateInstructorLastName(email, lastName);
        instructor = instructorService.getInstructor(email);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

    @PutMapping("/instructors/{email}/password")
    public InstructorRequestDto updateInstructorPassword(@PathVariable String email, @RequestBody String password) {
        Instructor instructor = instructorService.getInstructor(email);
        instructorService.updateInstructorPassword(email, password);
        instructor = instructorService.getInstructor(email);
        return new InstructorRequestDto(instructor.getEmail(), instructor.getFirstName(), instructor.getLastName(), instructor.getPassword());
    }

}
