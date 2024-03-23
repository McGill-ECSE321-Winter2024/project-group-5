package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Instructor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private LoginRepository loginRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loginRepository.deleteAll();
        clientRepository.deleteAll();
        instructorRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private static final String LOGIN_TYPE = "INSTRUCTOR";
    private static final String LOGIN_EMAIL = "instructor@sportplus.com";
    private static final String LOGIN_PASSWORD = "Pass123";
    private static final Time LOGIN_CURRENTTIME = Time.valueOf("10:30:00");

    private static final String INSTRUCTOR_EMAIL = LOGIN_EMAIL;
    private static final String INSTRUCTOR_FISTNAME = "John";
    private static final String INSTRUCTOR_LASTNAME = "Doe";
    private static final String INSTRUCTOR_PASSWORD = LOGIN_PASSWORD;

    private int INSTRUCTOR_VALID_ACCOUNTID = 0;

    @Test
    public void testLogIn() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);
        assertNotNull(instructor);
        instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
        assertNotNull(instructor);

        LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_CURRENTTIME);

        ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request, LoginResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LoginResponseDto createdLogin = response.getBody();
        assertNotNull(createdLogin);
        assertEquals(createdLogin.getAccountEmail(), LOGIN_EMAIL);
        assertEquals(createdLogin.getAccountType(), LOGIN_TYPE);
        assertNotNull(createdLogin.getLoginId());
        assertTrue(createdLogin.getLoginId() > 0);
        assertNotNull(loginRepository.findByAccount(instructor));
    }

    @Test
    public void testLogOut() {
        // create Instructor and save to repo
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
        instructorRepository.save(instructor);
        assertNotNull(instructor);
        instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
        assertNotNull(instructor);

        // create login for instructor through rest controller
        LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD, LOGIN_CURRENTTIME);
        ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request, LoginResponseDto.class);
        assertNotNull(response);
        assertNotNull(loginRepository.findByAccount(instructor));
        assertTrue(loginRepository.findAll().size() == 1);

        // request deletion of login in repo
        String url = "/logout/" + String.valueOf(loginRepository.findByAccount(instructor).getLoginId());
        client.delete(url);

        // assert that login has been deleted from repo
        assertNull(loginRepository.findByAccount(instructor));
        assertTrue(loginRepository.findAll().size() == 0);
    }

}
