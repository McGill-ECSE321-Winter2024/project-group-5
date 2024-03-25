package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
                // Clear database
                instructorRepository.deleteAll();
        }

        // Create some global variables
        private static final String INSTRUCTOR_EMAIL = "example@sportplus.com";
        private static final String INSTRUCTOR_FISTNAME = "John";
        private static final String INSTRUCTOR_LASTNAME = "Doe";
        private static final String INSTRUCTOR_PASSWORD = "Password123";

        private int INSTRUCTOR_VALID_ACCOUNTID;

        @Test
        public void testFindInstructorByEmail() {

                // Create new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Set the url
                String url = "/instructors/getByEmail/" + String.valueOf(INSTRUCTOR_EMAIL);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorResponseDto instructorResponse = response.getBody();
                assertNotNull(instructorResponse);
                assertTrue(instructorResponse.getAccountId() > 0);
                assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
                assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
                assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        }

        @Test
        public void testFindInstructorByEmail2() {

                // Create new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Set the url
                String url = "/instructors/getByEmail/" + String.valueOf(INSTRUCTOR_EMAIL) + "/";

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorResponseDto instructorResponse = response.getBody();
                assertNotNull(instructorResponse);
                assertTrue(instructorResponse.getAccountId() > 0);
                assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
                assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
                assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        }

        @Test
        public void testFindInstructorByAccountId() {
                // Create new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Get the id for the url
                int validId = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getAccountId();

                // Set the url
                String url = "/instructors/getById/" + String.valueOf(validId);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorResponseDto instructorResponse = response.getBody();
                assertNotNull(instructorResponse);
                assertEquals(validId, instructorResponse.getAccountId());
                assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
                assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
                assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        }

        @Test
        public void testFindInstructorByAccountId2() {
                // Create new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Get the id for the url
                int validId = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getAccountId();

                // Set the url
                String url = "/instructors/getById/" + String.valueOf(validId) + "/";

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.getForEntity(url, InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorResponseDto instructorResponse = response.getBody();
                assertNotNull(instructorResponse);
                assertEquals(validId, instructorResponse.getAccountId());
                assertEquals(INSTRUCTOR_EMAIL, instructorResponse.getEmail());
                assertEquals(INSTRUCTOR_FISTNAME, instructorResponse.getFirstName());
                assertEquals(INSTRUCTOR_LASTNAME, instructorResponse.getLastName());
        }

        @Test
        public void testGetAllInstructors() {

                // Create 2 instructors
                Instructor instructor1 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                Instructor instructor2 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor1);
                instructorRepository.save(instructor2);

                // Set the url
                String url = "/instructors/all";

                // Get the response
                ResponseEntity<InstructorListDto> response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
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
                }
        }

        @Test
        public void testGetAllInstructors2() {

                // Create 2 instructors
                Instructor instructor1 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                Instructor instructor2 = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor1);
                instructorRepository.save(instructor2);

                // Set the url
                String url = "/instructors/all/";

                // Get the response
                ResponseEntity<InstructorListDto> response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
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
                }
        }

        @Test
        public void testDeleteInstructorByEmail() {

                // Create a new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Set the url
                String url = "/instructors/all";

                // Get the response
                ResponseEntity<InstructorListDto> response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorListDto instructorResponse = response.getBody();
                assertTrue(instructorResponse.getInstructors().size() == 1);
                assertTrue(instructorResponse.getInstructors().get(0).getEmail().equals(INSTRUCTOR_EMAIL));

                // Set the delte url
                String urlToDelete = "/instructors/delete/" + INSTRUCTOR_EMAIL;

                // Delete the instructor
                client.delete(urlToDelete);

                // Get the response
                response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                instructorResponse = response.getBody();
                assertTrue(instructorResponse.getInstructors().size() == 0);
        }

        @Test
        public void testDeleteInstructorByEmail2() {

                // Create a new instructor
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);

                // Set the url
                String url = "/instructors/all";

                // Get the response
                ResponseEntity<InstructorListDto> response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                InstructorListDto instructorResponse = response.getBody();
                assertTrue(instructorResponse.getInstructors().size() == 1);
                assertTrue(instructorResponse.getInstructors().get(0).getEmail().equals(INSTRUCTOR_EMAIL));

                // Set the delete url
                String urlToDelete = "/instructors/delete/" + INSTRUCTOR_EMAIL + "/";

                // Delete the client
                client.delete(urlToDelete);

                // Get the response
                response = client.getForEntity(url, InstructorListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                instructorResponse = response.getBody();
                assertTrue(instructorResponse.getInstructors().size() == 0);
        }

        @Test
        public void testCreateInstructor() {

                // Create a request Instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertEquals(createdInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(createdInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(createdInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(createdInstructor.getAccountId());
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        }

        @Test
        public void testCreateInstructor2() {

                // Create a request Instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create/", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertEquals(createdInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(createdInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(createdInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(createdInstructor.getAccountId());
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
        }

        @Test
        public void testUpdateInstructorFirstName() {
                // Create a request Instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create a new first name
                String newFirstName = "JohnTheNew";

                // set the url to update the name
                String url = "/instructors/updateFirstName/" + INSTRUCTOR_EMAIL + "/" + newFirstName;

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), newFirstName);
                assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getFirstName(), newFirstName);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

        @Test
        public void testUpdateInstructorFirstName2() {

                // Create a request Instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create a new first name
                String newFirstName = "JohnTheNew";

                // set the url to update the name
                String url = "/instructors/updateFirstName/" + INSTRUCTOR_EMAIL + "/" + newFirstName + "/";

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), newFirstName);
                assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getFirstName(), newFirstName);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

        @Test
        public void testUpdateInstructorLastName() {

                // Create new request instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create new last name
                String newLastName = "DoeTheNew";

                // Set the url to update last name
                String url = "/instructors/updateLastName/" + INSTRUCTOR_EMAIL + "/" + newLastName;

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(updatedInstructor.getLastName(), newLastName);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getLastName(), newLastName);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

        @Test
        public void testUpdateInstructorLastName2() {

                // Create new request instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create new last name
                String newLastName = "DoeTheNew";

                // Set the url to update last name
                String url = "/instructors/updateLastName/" + INSTRUCTOR_EMAIL + "/" + newLastName + "/";

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(updatedInstructor.getLastName(), newLastName);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getLastName(), newLastName);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

        @Test
        public void testUpdateInstructorPassword() {

                // Create new request instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create new password
                String newPassword = "TheNewPass456";

                // Set the url to update password
                String url = "/instructors/updatePassword/" + INSTRUCTOR_EMAIL + "/" + INSTRUCTOR_PASSWORD + "/"
                                + newPassword;

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getPassword(), newPassword);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

        @Test
        public void testUpdateInstructorPassword2() {
                // Create new request instructor
                InstructorRequestDto request = new InstructorRequestDto(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_PASSWORD);

                // Get the response
                ResponseEntity<InstructorResponseDto> response = client.postForEntity("/instructors/create", request,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                InstructorResponseDto createdInstructor = response.getBody();
                assertNotNull(createdInstructor);
                assertTrue(createdInstructor.getAccountId() > 0, "Response should have a positive ID.");
                INSTRUCTOR_VALID_ACCOUNTID = createdInstructor.getAccountId();

                // Create new password
                String newPassword = "TheNewPass456";

                // Set the url to update password
                String url = "/instructors/updatePassword/" + INSTRUCTOR_EMAIL + "/" + INSTRUCTOR_PASSWORD + "/"
                                + newPassword
                                + "/";

                // Get the response
                ResponseEntity<InstructorResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                InstructorResponseDto.class);

                // Validate the response
                assertNotNull(responseAfterUpdate);
                assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
                InstructorResponseDto updatedInstructor = responseAfterUpdate.getBody();
                assertNotNull(updatedInstructor);
                assertEquals(updatedInstructor.getEmail(), INSTRUCTOR_EMAIL);
                assertEquals(updatedInstructor.getFirstName(), INSTRUCTOR_FISTNAME);
                assertEquals(updatedInstructor.getLastName(), INSTRUCTOR_LASTNAME);
                assertNotNull(updatedInstructor.getAccountId());
                assertTrue(updatedInstructor.getAccountId() > 0, "Response should have a positive ID.");
                assertNotNull(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL));
                assertEquals(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL).getPassword(), newPassword);
                assertTrue(instructorRepository.findAll().size() == 1);
        }

}
