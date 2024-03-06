package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    private static final String INSTRUCTOR_EMAIL = "example@email.com";
    private static final String INSTRUCTOR_FISTNAME = "John";
    private static final String INSTRUCTOR_LASTNAME = "Doe";
    private static final String INSTRUCTOR_PASSWORD = "password123";

    private static final String NOT_INSTRUCTOR_EMAIL = "notemail@email.com";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(instructorRepository.findInstructorByEmail(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(INSTRUCTOR_EMAIL)) {
                Instructor instructor = new Instructor();
                instructor.setEmail(INSTRUCTOR_EMAIL);
                instructor.setFirstName(INSTRUCTOR_FISTNAME);
                instructor.setLastName(INSTRUCTOR_LASTNAME);
                instructor.setPassword(INSTRUCTOR_PASSWORD);
                return instructor;
            } else {
                return null;
            }
        });
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(instructorRepository.save(any(Instructor.class))).thenAnswer(returnParameterAsAnswer);
    }
    
    @SuppressWarnings("null")
    @Test
	public void testCreateInstructor() {
        String email = "newinstructor@email.com";
        String firstName = "Paul";
        String lastName = "Dmyt";
        String password = "Ro1234";

		assertEquals(0, instructorService.getAllInstructors().size());

        Instructor instructor = null;
		try {
            instructor = instructorService.createInstructor(email, firstName, password, lastName);
		} catch (IllegalArgumentException e) {
			fail();
		}
		assertNotNull(instructor);
		assertEquals(email, instructor.getEmail());
        assertEquals(firstName, instructor.getFirstName());
        assertEquals(lastName, instructor.getLastName());
        assertEquals(password, instructor.getPassword());
        verify(instructorRepository, times(1)).save(instructor);
	}

    @Test
    public void testReadInstructorById() {
        int accountId = 2810;
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, accountId);
        when(instructorRepository.findByAccountId(accountId)).thenReturn(instructor);

        Instructor searchInstructor = instructorService.getInstructor(accountId);

        assertNotNull(searchInstructor);
        assertEquals(instructor.getAccountId(), searchInstructor.getAccountId());
        assertEquals(instructor.getEmail(), searchInstructor.getEmail());
        assertEquals(instructor.getFirstName(), searchInstructor.getFirstName());
        assertEquals(instructor.getLastName(), searchInstructor.getLastName());
        assertEquals(instructor.getPassword(), searchInstructor.getPassword());
    }

    @Test
	public void testGetExistingInstructor() {
		assertEquals(INSTRUCTOR_EMAIL, instructorService.getInstructor(INSTRUCTOR_EMAIL).getEmail());
	}

	@Test
	public void testGetNonExistingPerson() {
		assertNull(instructorService.getInstructor(NOT_INSTRUCTOR_EMAIL));
	}

    // add more tests from service

}
