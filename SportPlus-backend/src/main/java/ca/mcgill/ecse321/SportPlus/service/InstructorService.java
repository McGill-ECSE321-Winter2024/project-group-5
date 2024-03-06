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

    //-----------Wrappers-----------//

    @Transactional
	public Instructor getInstructor(String email) {
        Instructor instructor = instructorRepository.findInstructorByEmail(email);
        return instructor;
	}

    @Transactional
    public Instructor getInstructor(Integer accountId) {
        Instructor instructor = instructorRepository.findByAccountId(accountId);
        return instructor;
    }

    @Transactional
    public List<Instructor> getAllInstructors() {
        return HelperMethods.toList(instructorRepository.findAll());
    }

    @Transactional
    public void deleteInstructor(String email) {
        instructorRepository.deleteInstructorByEmail(email);
    }
    
    //------------EndWrappers----------//

    @Transactional
    public Instructor createInstructor(String email, String firstName, String password, String lastName) {
        String message1 = HelperMethods.ClientEmailCheck(email);
        String message2 = HelperMethods.PasswordCheck(password);
        if (message1.isEmpty() || message2.isEmpty()) {
            Instructor instructor = new Instructor();
            instructor.setEmail(email);
            instructor.setFirstName(firstName);
            instructor.setPassword(password);
            instructor.setLastName(lastName);
            instructorRepository.save(instructor);
            return instructor;
        }
        return null;
    }

    @Transactional
    public Instructor updateInstructorEmail(int accountId, String email) {
        String message = HelperMethods.ClientEmailCheck(email);
        if (message.isEmpty()) {
            Instructor instructor = getInstructor(accountId);
            instructor.setEmail(email);
            return instructor;
        }
        return null;
    }

    @Transactional
    public Instructor updateInstructorFirstName(String email, String firstName) {
        Instructor instructor = getInstructor(email);
        instructor.setFirstName(firstName);
        return instructor;
    }

    @Transactional
    public Instructor updateInstructorLastName(String email, String lastName) {
        Instructor instructor = getInstructor(email);
        instructor.setLastName(lastName);
        return instructor;
    }

    @Transactional
    public Instructor updateInstructorPassword(String email, String password) {
        String message = HelperMethods.PasswordCheck(password);
        if (message.isEmpty()) {
            Instructor instructor = getInstructor(email);
            instructor.setPassword(password);
            return instructor;
        }
        return null;
    }
    
}
