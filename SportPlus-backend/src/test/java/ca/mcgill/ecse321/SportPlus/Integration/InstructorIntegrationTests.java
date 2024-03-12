package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dto.InstructorListDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.InstructorResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class InstructorIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
    }

    private static final String INSTRUCTOR_EMAIL = "example@sportplus.com";
    private static final String INSTRUCTOR_FISTNAME = "John";
    private static final String INSTRUCTOR_LASTNAME = "Doe";
    private static final String INSTRUCTOR_PASSWORD = "Password123";

    private int INSTRUCTOR_VALID_ACCOUNTID;

    @Test
    public void testFindInstructorByEmail() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);

        String url = "/instructors/getByEmail/" + String.valueOf(INSTRUCTOR_EMAIL);

        ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorResponseDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        assertTrue(instructorResponse.getAccountId() > 0);
        assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
        assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
        assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        assertEquals(INSTRUCTOR_PASSWORD, instructorResponse.getPassword());
    }

    @Test
    public void testFindInstructorByEmail2() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);

        String url = "/instructors/getByEmail/" + String.valueOf(INSTRUCTOR_EMAIL) + "/";

        ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorResponseDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        assertTrue(instructorResponse.getAccountId() > 0);
        assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
        assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
        assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        assertEquals(INSTRUCTOR_PASSWORD, instructorResponse.getPassword());
    }

    @Test
    public void testFindInstructorByAccountId() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);
        int validId = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getAccountId();

        String url = "/instructors/getById/" + String.valueOf(validId);

        ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorResponseDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        assertEquals(validId, instructorResponse.getAccountId());
        assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
        assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
        assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        assertEquals(INSTRUCTOR_PASSWORD, instructorResponse.getPassword());
    }

    @Test
    public void testFindInstructorByAccountId2() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);
        int validId = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getAccountId();

        String url = "/instructors/getById/" + String.valueOf(validId) + "/";

        ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorResponseDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        assertEquals(validId, instructorResponse.getAccountId());
        assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
        assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
        assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        assertEquals(INSTRUCTOR_PASSWORD, instructorResponse.getPassword());
    }

    @Test
    public void testGetAllInstructors() {
        Instructor instructor1 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        Instructor instructor2 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);

        String url = "/instructors/all";

        ResponseEntity<InstructorListDto> response = client.getForEntity(url,InstructorListDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorListDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        for (InstructorResponseDto instructor : instructorResponse.getInstructors()) {
            assertNotNull(instructor);
            assertTrue(instructor.getAccountId() > 0);
            assertEquals(INSTRUCTOR_EMAIL, instructor.getEmail());
            assertEquals(INSTRUCTOR_FISTNAME, instructor.getFirstName());
            assertEquals(INSTRUCTOR_LASTNAME, instructor.getLastName());
            assertEquals(INSTRUCTOR_PASSWORD, instructor.getPassword());
        }
    }

    @Test
    public void testGetAllInstructors2() {
        Instructor instructor1 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        Instructor instructor2 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor1);
        instructorRepository.save(instructor2);

        String url = "/instructors/all/";

        ResponseEntity<InstructorListDto> response = client.getForEntity(url,InstructorListDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorListDto instructorResponse = response.getBody();
        assertNotNull(instructorResponse);
        for (InstructorResponseDto instructor : instructorResponse.getInstructors()) {
            assertNotNull(instructor);
            assertTrue(instructor.getAccountId() > 0);
            assertEquals(INSTRUCTOR_EMAIL, instructor.getEmail());
            assertEquals(INSTRUCTOR_FISTNAME, instructor.getFirstName());
            assertEquals(INSTRUCTOR_LASTNAME, instructor.getLastName());
            assertEquals(INSTRUCTOR_PASSWORD, instructor.getPassword());
        }
    }

    @Test
    public void testDeleteInstructorByEmail() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);

        String url = "/instructors/all";

        ResponseEntity<InstructorListDto> response = client.getForEntity(url,InstructorListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorListDto instructorResponse = response.getBody();
        assertTrue(instructorResponse.getInstructors().size() == 1);
        assertTrue(instructorResponse.getInstructors().get(0).getEmail().equals(INSTRUCTOR_EMAIL));

        String urlToDelete = "/instructors/delete/" + INSTRUCTOR_EMAIL;

        client.delete(urlToDelete);

        response = client.getForEntity(url,InstructorListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        instructorResponse = response.getBody();
        assertTrue(instructorResponse.getInstructors().size() == 0);
    }

    @Test
    public void testDeleteInstructorByEmail2() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD, INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);

        String url = "/instructors/all";

        ResponseEntity<InstructorListDto> response = client.getForEntity(url,InstructorListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        InstructorListDto instructorResponse = response.getBody();
        assertTrue(instructorResponse.getInstructors().size() == 1);
        assertTrue(instructorResponse.getInstructors().get(0).getEmail().equals(INSTRUCTOR_EMAIL));

        String urlToDelete = "/instructors/delete/" + INSTRUCTOR_EMAIL + "/";

        client.delete(urlToDelete);

        response = client.getForEntity(url,InstructorListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        instructorResponse = response.getBody();
        assertTrue(instructorResponse.getInstructors().size() == 0);
    }

    @Test
    public void testCreateInstructor() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertEquals(createdInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(createdInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(createdInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(createdInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(createdInstructor.getAccountId());
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
    }

    @Test
    public void testCreateInstructor2() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create/", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertEquals(createdInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(createdInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(createdInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(createdInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(createdInstructor.getAccountId());
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
    }

    @Test
    public void testUpdateInstructorEmail() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newEmail = "exampleNew@sportplus.com";

        String url = "/instructors/updateEmail/" + INSTRUCTOR_VALID_ACCOUNTID + "/" + newEmail;

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), newEmail);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertNotNull(instructorRepository.findInstructorByEmail(newEmail));
    }

    @Test
    public void testUpdateInstructorEmail2() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newEmail = "exampleNew@sportplus.com";

        String url = "/instructors/updateEmail/" + INSTRUCTOR_VALID_ACCOUNTID + "/" + newEmail + "/";

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), newEmail);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertNotNull(instructorRepository.findInstructorByEmail(newEmail));
    }

    @Test
    public void testUpdateInstructorFirstName() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/instructors/updateFirstName/" + INSTRUCTOR_EMAIL + "/" + newFirstName;

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), newFirstName);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getFirstName(), newFirstName);
        assertTrue(instructorRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateInstructorFirstName2() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/instructors/updateFirstName/" + INSTRUCTOR_EMAIL + "/" + newFirstName + "/";

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), newFirstName);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getFirstName(), newFirstName);
        assertTrue(instructorRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateInstructorLastName() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/instructors/updateLastName/" + INSTRUCTOR_EMAIL + "/" + newLastName;

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), newLastName);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getLastName(), newLastName);
        assertTrue(instructorRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateInstructorLastName2() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/instructors/updateLastName/" + INSTRUCTOR_EMAIL + "/" + newLastName + "/";

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), newLastName);
        assertEquals(updatedInstructor.getPassword(), INSTRUCTOR_PASSWORD);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getLastName(), newLastName);
        assertTrue(instructorRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateInstructorPassword() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/instructors/updatePassword/" + INSTRUCTOR_EMAIL + "/" + newPassword;

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), newPassword);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getPassword(), newPassword);
        assertTrue(instructorRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateInstructorPassword2() {
        InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

        ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request, InstructorResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        InstructorResponseDto createdInstructor = response.getBody();
        assertNotNull(createdInstructor);
        assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
        INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/instructors/updatePassword/" + INSTRUCTOR_EMAIL + "/" + newPassword + "/";

        ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, InstructorResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
        assertNotNull(updatedInstructor);
        assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
        assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
        assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
        assertEquals(updatedInstructor.getPassword(), newPassword);
        assertNotNull(updatedInstructor.getAccountId());
        assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getPassword(), newPassword);
        assertTrue(instructorRepository.findAll().size() == 1);
    }
    
}
