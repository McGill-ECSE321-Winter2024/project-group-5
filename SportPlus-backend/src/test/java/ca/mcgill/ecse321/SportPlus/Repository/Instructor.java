package ca.mcgill.ecse321.SportPlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;

    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadInstructor() {
        // Create instructor.
        String firstname = "Jane";
        Integer userId = 33;
        String password = "InstructorPass";
        String lastName = "Doe";
        String email = "jane.doe@example.com";

        Instructor instructor = new Instructor(email, firstname, password, lastName, userId);

        // Save instructor
        instructorRepository.save(instructor);

        // Read instructor from database.
        Instructor loadedInstructor = instructorRepository.findInstructorByEmail(email);

        // Assert that instructor is not null and has correct attributes.
        assertNotNull(loadedInstructor);
        assertEquals(firstname, loadedInstructor.getFirstname());
        assertEquals(userId, loadedInstructor.getUserId());
        assertEquals(password, loadedInstructor.getPassword());
        assertEquals(lastName, loadedInstructor.getLastName());
        assertEquals(email, loadedInstructor.getEmail());
    }
}