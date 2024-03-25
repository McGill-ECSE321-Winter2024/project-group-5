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

import ca.mcgill.ecse321.SportPlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorListDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorRequestDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.service.InstructorService;

@CrossOrigin(origins = "*")
@RestController
public class InstructorRestController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping(value = { "/instructors/getByEmail/{email}", "/instructors/getByEmail/{email}/" })
    public InstructorResponseDto findInstructorByEmail(@PathVariable("email") String theEmail) {

        // Get the instructor
        Instructor instructor = instructorService.getInstructor(theEmail);

        // Return the response
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/getById/{accountId}", "/instructors/getById/{accountId}/" })
    public InstructorResponseDto findInstructorByAccountId(@PathVariable("accountId") int theId) {

        // Get the instructor with id
        Instructor instructor = instructorService.getInstructor(theId);
        // Return the response
        return new InstructorResponseDto(instructor);
    }

    @GetMapping(value = { "/instructors/all", "/instructors/all/" })
    public InstructorListDto getAllInstructors() {

        // Get all instructors with ids
        List<InstructorResponseDto> instructors = new ArrayList<>();
        for (Instructor instructor : instructorService.getAllInstructors()) {
            instructors.add(new InstructorResponseDto(instructor));
        }

        // Return list of responses
        return new InstructorListDto(instructors);
    }

    @DeleteMapping(value = { "/instructors/delete/{email}", "/instructors/delete/{email}/" })
    public void deleteInstructorByEmail(@PathVariable("email") String theEmail) {

        // delte the instructor by email
        instructorService.deleteInstructor(theEmail);
    }

    @PostMapping(value = { "/instructors/create", "/instructors/create/" })
    @ResponseStatus(HttpStatus.CREATED)
    public InstructorResponseDto createInstructor(@RequestBody InstructorRequestDto instructor) {

        // Create an instructor
        Instructor createdInstructor = instructorService.createInstructor(instructor.getEmail(),
                instructor.getFirstName(), instructor.getPassword(), instructor.getLastName());

        // Return the response
        return new InstructorResponseDto(createdInstructor);
    }

    @PutMapping(value = { "/instructors/updateFirstName/{email}/{newFirstName}",
            "/instructors/updateFirstName/{email}/{newFirstName}/" })
    public InstructorResponseDto updateInstructorFirstName(@PathVariable("email") String theEmail,
            @PathVariable("newFirstName") String theFirstName) {

        // Get instructor by email
        Instructor instructor = instructorService.getInstructor(theEmail);

        // Update the first name
        instructorService.updateInstructorFirstName(theEmail, theFirstName);
        instructor = instructorService.getInstructor(theEmail);

        // return the response
        return new InstructorResponseDto(instructor);
    }

    @PutMapping(value = { "/instructors/updateLastName/{email}/{newLastName}",
            "/instructors/updateLastName/{email}/{newLastName}/" })
    public InstructorResponseDto updateInstructorLastName(@PathVariable("email") String theEmail,
            @PathVariable("newLastName") String theLastName) {

        // Get instructor by email
        Instructor instructor = instructorService.getInstructor(theEmail);

        // Update the last name
        instructorService.updateInstructorLastName(theEmail, theLastName);
        instructor = instructorService.getInstructor(theEmail);

        // Return response
        return new InstructorResponseDto(instructor);
    }

    @PutMapping(value = { "/instructors/updatePassword/{email}/{oldPassword}/{newPassword}",
            "/instructors/updatePassword/{email}/{oldPassword}/{newPassword}/" })
    public InstructorResponseDto updateInstructorPassword(@PathVariable("email") String theEmail,
            @PathVariable("oldPassword") String theOldPassword, @PathVariable("newPassword") String thePassword) {

        // Get instructor by email
        Instructor instructor = instructorService.getInstructor(theEmail);

        // Update password
        instructorService.updateInstructorPassword(theEmail, theOldPassword, thePassword);
        instructor = instructorService.getInstructor(theEmail);

        // Return response
        return new InstructorResponseDto(instructor);
    }

}
