package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.service.InstructorService;

@ExtendWith(MockitoExtension.class)
public class TestInstructorService {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    // SAet up gloval variables
    private static final String INSTRUCTOR_EMAIL = "example@sportplus.com";
    private static final String INSTRUCTOR_FISTNAME = "John";
    private static final String INSTRUCTOR_LASTNAME = "Doe";
    private static final String INSTRUCTOR_PASSWORD = "password123";
    private static final int INSTRUCTOR_ACCOUNTID = 2;
    private static final String NOT_INSTRUCTOR_EMAIL = "notemail@sportplus.com";

    @BeforeEach
    public void setMockOutput() {

        // Setup MockOutput
        lenient().when(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL)).thenReturn(new Instructor(
                INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID));
        lenient().when(instructorRepository.findByAccountId(INSTRUCTOR_ACCOUNTID)).thenReturn(new Instructor(
                INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID));
        lenient().when(instructorRepository.findAll()).thenReturn(Arrays.asList(new Instructor(INSTRUCTOR_EMAIL,
                INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID)));
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(instructorRepository.save(any(Instructor.class))).thenAnswer(returnParameterAsAnswer);
    }

    @Test
    public void testCreateInstructor() {

        // Create an instrcutor
        String email = "newinstructor@sportplus.com";
        String firstName = "Paul";
        String lastName = "Dmyt";
        String password = "Ro1234";

        Instructor instructor = instructorService.createInstructor(email, firstName, password, lastName);

        // Vrifiy if the attributes match
        assertNotNull(instructor);
        assertEquals(email, instructor.getEmail());
        assertEquals(firstName, instructor.getFirstName());
        assertEquals(lastName, instructor.getLastName());
        assertEquals(password, instructor.getPassword());

        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    public void testUpdateInstructor() {

        // Create a new instructor
        String newPassword = "NewPass123";
        String newFirstName = "NewJohn";
        String newLastName = "NewDoe";

        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Instructor updatedInstructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);

        // Update the instrcutorl firstname, lastname, password
        instructorService.updateInstructorFirstName(INSTRUCTOR_EMAIL, newFirstName);
        instructorService.updateInstructorLastName(INSTRUCTOR_EMAIL, newLastName);
        instructorService.updateInstructorPassword(INSTRUCTOR_EMAIL, INSTRUCTOR_PASSWORD, newPassword);

        // Validate the attributes
        assertNotNull(updatedInstructor);
        assertEquals(instructor.getEmail(), updatedInstructor.getEmail());
        assertEquals(instructor.getAccountId(), updatedInstructor.getAccountId());
        assertNotEquals(instructor.getFirstName(), updatedInstructor.getFirstName());
        assertNotEquals(instructor.getLastName(), updatedInstructor.getLastName());
        assertNotEquals(instructor.getPassword(), updatedInstructor.getPassword());
        assertEquals(newPassword, updatedInstructor.getPassword());
        assertEquals(newFirstName, updatedInstructor.getFirstName());
        assertEquals(newLastName, updatedInstructor.getLastName());

        verify(instructorRepository, times(3)).save(any(Instructor.class));
    }

    @Test
    public void testDeleteInstructor() {

        // Create a new instructor
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);

        // Delete the instructor
        instructorService.deleteInstructor(instructor.getEmail());

        // Check if doesnt exist
        verify(instructorRepository, times(1)).deleteInstructorByEmail(INSTRUCTOR_EMAIL);
    }

    @Test
    public void testGetExistingInstructor() {

        // Check if an instructor exists with email
        assertEquals(INSTRUCTOR_EMAIL, instructorService.getInstructor(INSTRUCTOR_EMAIL).getEmail());
    }

    @Test
    public void testGetNonExistingInstructor() {

        // Check for non existing instructor
        assertNull(instructorService.getInstructor(NOT_INSTRUCTOR_EMAIL));
    }

    @Test
    public void testReadInstructorById() {

        // Create an instructor
        int accountId = INSTRUCTOR_ACCOUNTID;

        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, accountId);

        // Get the instructor
        Instructor searchInstructor = instructorService.getInstructor(accountId);

        // Verify the attributes
        assertNotNull(searchInstructor);
        assertEquals(instructor.getAccountId(), searchInstructor.getAccountId());
        assertEquals(instructor.getEmail(), searchInstructor.getEmail());
        assertEquals(instructor.getFirstName(), searchInstructor.getFirstName());
        assertEquals(instructor.getLastName(), searchInstructor.getLastName());
        assertEquals(instructor.getPassword(), searchInstructor.getPassword());
    }

    @Test
    public void testGetAllInstructors() {

        // Create an instructor
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        List<Instructor> instructors = Arrays.asList(instructor);

        // Get the instructors
        List<Instructor> instructorsFromRepo = instructorService.getAllInstructors();

        // Validate if found correctly
        assertNotNull(instructorsFromRepo);
        assertEquals(instructors, instructorsFromRepo);
    }

}
