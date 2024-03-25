package ca.mcgill.ecse321.SportPlus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.service.utilities.HelperMethods;

@Service
public class InstructorService {

    @Autowired
    InstructorRepository instructorRepository;

    // -----------Wrappers-----------//

    @Transactional
    public Instructor getInstructor(String email) {

        // Validate the input
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Instructor email cannot be empty!");
        }
        // Find instrcutor by email
        Instructor instructor = instructorRepository.findInstructorByEmail(email);
        return instructor;
    }

    @Transactional
    public Instructor getInstructor(Integer accountId) {

        // Validate input
        if (accountId < 0) {
            throw new IllegalArgumentException("Account Id cannot be less than 0!");
        }
        // Find with account id
        Instructor instructor = instructorRepository.findByAccountId(accountId);
        return instructor;
    }

    @Transactional
    public List<Instructor> getAllInstructors() {

        // Get all the instructors to a list
        return HelperMethods.toList(instructorRepository.findAll());
    }

    @Transactional
    public void deleteInstructor(String email) {

        // Validate the email
        if (email == null || email.trim().length() == 0) {
            throw new IllegalArgumentException("Person name cannot be empty!");
        }
        // delte the instructor
        instructorRepository.deleteInstructorByEmail(email);
    }

    // ------------EndWrappers----------//

    @Transactional
    public Instructor createInstructor(String email, String firstName, String password, String lastName) {

        // Validate the input
        if (email == null || HelperMethods.InstructorEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid password!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Instructor first name cannot be empty!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Instructor last name cannot be empty!");
        }
        // Create an instructor
        Instructor instructor = new Instructor();
        instructor.setEmail(email);
        instructor.setFirstName(firstName);
        instructor.setPassword(password);
        instructor.setLastName(lastName);
        instructorRepository.save(instructor);
        return instructor;
    }

    @Transactional
    public Instructor updateInstructorFirstName(String email, String firstName) {
        if (email == null || HelperMethods.InstructorEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        Instructor instructor = getInstructor(email);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor with email does not exist!");
        }
        if (firstName == null || firstName.trim().length() == 0) {
            throw new IllegalArgumentException("Instructor first name cannot be empty!");
        }
        instructor.setFirstName(firstName);
        instructorRepository.save(instructor);
        return instructor;
    }

    @Transactional
    public Instructor updateInstructorLastName(String email, String lastName) {

        // Validate the input
        if (email == null || HelperMethods.InstructorEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        Instructor instructor = getInstructor(email);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor with email does not exist!");
        }
        if (lastName == null || lastName.trim().length() == 0) {
            throw new IllegalArgumentException("Instructor last name cannot be empty!");
        }

        // Set the last name & save the instructor
        instructor.setLastName(lastName);
        instructorRepository.save(instructor);
        return instructor;
    }

    @Transactional
    public Instructor updateInstructorPassword(String email, String oldPassword, String password) {

        // Validate the input
        if (email == null || HelperMethods.InstructorEmailCheck(email).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid email!");
        }
        Instructor instructor = getInstructor(email);
        if (instructor == null) {
            throw new IllegalArgumentException("Instructor with email does not exist!");
        }
        if (!instructor.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Wrong old password!");
        }
        if (password == null || HelperMethods.PasswordCheck(password).trim().length() != 0) {
            throw new IllegalArgumentException("Invalid new password!");
        }

        // Set the password & save the instructor
        instructor.setPassword(password);
        instructorRepository.save(instructor);
        return instructor;
    }

}
