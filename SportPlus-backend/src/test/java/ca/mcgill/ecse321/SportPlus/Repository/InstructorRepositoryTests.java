package ca.mcgill.ecse321.SportPlus.Repository;

import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest
public class InstructorRepositoryTests {
    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }

    @Test
    public void testFindInstructorByEmail() {
        String aEmail = "instructor.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Instructor instructor = new Instructor(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        instructorRepository.save(instructor);

        Instructor instructorFromDb = instructorRepository.findInstructorByEmail(aEmail);

        assertNotNull(instructorFromDb);
        assertEquals(aEmail, instructorFromDb.getEmail());
        assertEquals(aFirstName, instructorFromDb.getFirstName());
        assertEquals(aPassword, instructorFromDb.getPassword());
        assertEquals(aLastName, instructorFromDb.getLastName());
    }

    @Test
    @Transactional
    public void testDeleteInstructorByEmail() {

        String aEmail = "instructor.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Instructor instructorToBeDeleted = new Instructor(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        Instructor InstrucotrToBeSaved = new Instructor("anEmail", "aFirstName", "aPassword", "aLastName", 0);

        instructorRepository.save(instructorToBeDeleted);
        instructorRepository.save(InstrucotrToBeSaved);

        instructorRepository.deleteInstructorByEmail(aEmail);

        assertThat(instructorRepository.findInstructorByEmail(aEmail)).isNull();

        // Checks if aClientToBeSaved still there
        assertThat(instructorRepository.findAll()).hasSize(1);
        Instructor remaininginstructor = instructorRepository.findInstructorByEmail("anEmail");
        assertThat(remaininginstructor).isNotNull();
        assertThat(remaininginstructor.getEmail()).isEqualTo("anEmail");
    }

    @Test
    public void findAll() {

        // Given 4 instrucotrs
        Instructor instructor1 = new Instructor("test", "John", "123", "Fie", 0);
        Instructor instructor2 = new Instructor("test1", "John1", "123", "Fie1", 0);
        Instructor instructor3 = new Instructor("test2", "John2", "123", "Fie2", 0);
        Instructor instructor4 = new Instructor("test4", "John3", "123", "Fie3", 0);

        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);
        instructorRepository.save(instructor3);
        instructorRepository.save(instructor4);

        List<Instructor> allInstructors = instructorRepository.findAll();

        assertThat(allInstructors).hasSize(4);

        // Checks the emails
        assertThat(allInstructors).extracting(Instructor::getEmail)
                .containsExactlyInAnyOrder(instructor1.getEmail(), instructor2.getEmail(), instructor3.getEmail(),
                        instructor4.getEmail());

        // Checks the names
        assertThat(allInstructors).extracting(Instructor::getFirstName)
                .containsExactlyInAnyOrder(instructor1.getFirstName(), instructor2.getFirstName(),
                        instructor3.getFirstName(),
                        instructor4.getFirstName());

        // Checks the last names
        assertThat(allInstructors).extracting(Instructor::getLastName)
                .containsExactlyInAnyOrder(instructor1.getLastName(), instructor2.getLastName(),
                        instructor3.getLastName(),
                        instructor4.getLastName());

        // Checks accountIDs
        assertThat(allInstructors).extracting(Instructor::getAccountId)
                .containsExactlyInAnyOrder(instructor1.getAccountId(), instructor2.getAccountId(),
                        instructor3.getAccountId(),
                        instructor4.getAccountId());

    }

    @Test
    @Transactional
    public void testFindByAccountId() {
        String aEmail = "instructor.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Instructor savedInstructor = new Instructor(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        instructorRepository.save(savedInstructor);
        int accountId = savedInstructor.getAccountId();

        Instructor instructorFromDb = instructorRepository.findByAccountId(accountId);

        assertNotNull(instructorFromDb);
        assertEquals(aEmail, instructorFromDb.getEmail());
        assertEquals(aFirstName, instructorFromDb.getFirstName());
        assertEquals(aPassword, instructorFromDb.getPassword());
        assertEquals(aLastName, instructorFromDb.getLastName());
        assertEquals(accountId, instructorFromDb.getAccountId());
    }

}
