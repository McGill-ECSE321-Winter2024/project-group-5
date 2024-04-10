package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.LoginListDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.LoginResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Login;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private RegistrationRepository registrationRepository;

        @Autowired
        private OwnerRepository ownerRepository;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private ClassTypeRepository classTypeRepository;

        @Autowired
        private SpecificClassRepository specificClassRepository;

        @Autowired
        private LoginRepository loginRepository;

        @Autowired
        private PaymentMethodRepository paymentMethodRepository;

        @BeforeEach
        @AfterEach
        public void clearDatabase() {
                loginRepository.deleteAll();
                registrationRepository.deleteAll();
                specificClassRepository.deleteAll();
                classTypeRepository.deleteAll();
                instructorRepository.deleteAll();
                paymentMethodRepository.deleteAll();
                clientRepository.deleteAll();
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
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                // verify that instructor indeed exists in repo
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create loggin request
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);

                // attempt login thorugh rest controller
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request,
                                LoginResponseDto.class);

                // veirfy that response is expected response
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
                // verify that instructor indeed exists in repo
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login request
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                // login through rest controller
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request,
                                LoginResponseDto.class);
                // confirm succesfull login
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

        @Test
        public void testgetLoginByAccount() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                // verify that instructor indeed exists in repo
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login request
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                // login thoruhg rest controller
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request,
                                LoginResponseDto.class);
                // confirm successfull login
                assertNotNull(response);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);

                // retrieve login by account through rest controller
                String url = "/login/getByAccount/" + INSTRUCTOR_EMAIL + "/INSTRUCTOR";
                ResponseEntity<LoginResponseDto> response2 = client.getForEntity(url, LoginResponseDto.class);
                // verify that response is ok & retrieved login is expected login
                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                LoginResponseDto response2Body = response2.getBody();
                assertNotNull(response2Body);
                assertEquals(INSTRUCTOR_EMAIL, response2Body.getAccountEmail());
                assertEquals(response2Body.getLoginId(), loginRepository.findByAccount(instructor).getLoginId());
                assertEquals(LOGIN_TYPE, response2Body.getAccountType());
        }

        @Test
        public void testGetLoginById() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                // verify that instructor indeed exists in repo
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login request
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                // login through rest controller
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login", request,
                                LoginResponseDto.class);
                // confrim successfull login
                assertNotNull(response);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                // retrieve login by id through rest controller
                String url = "/login/getById/" + id;
                ResponseEntity<LoginResponseDto> response2 = client.getForEntity(url, LoginResponseDto.class);
                // verify that response is ok & retrieved login is expected login
                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                LoginResponseDto response2Body = response2.getBody();
                assertNotNull(response2Body);
                assertEquals(INSTRUCTOR_EMAIL, response2Body.getAccountEmail());
                assertEquals(response2Body.getLoginId(), loginRepository.findByAccount(instructor).getLoginId());
                assertEquals(LOGIN_TYPE, response2Body.getAccountType());
        }

        @Test
        public void testGetAllLogins() {
                // create instructor, save to repo and confirm instructor indeed exists in repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create client, save to repo and confirm client indeed exists in repo
                Client user = new Client("example@mail.com", "firstname", "passWord4", "lastname", 0);
                clientRepository.save(user);
                user = clientRepository.findByEmail("example@mail.com");
                assertNotNull(user);

                // create login request for both client and instructor
                LoginRequestDto requestInstructor = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                LoginRequestDto requestClient = new LoginRequestDto("CLIENT", "example@mail.com", "passWord4",
                                LOGIN_CURRENTTIME);
                // login client and instrucotr through rest controller
                ResponseEntity<LoginResponseDto> responseInstructor = client.postForEntity("/login", requestInstructor,
                                LoginResponseDto.class);
                ResponseEntity<LoginResponseDto> responseClient = client.postForEntity("/login/", requestClient,
                                LoginResponseDto.class);
                // confirm successfull login of both client and login
                assertNotNull(responseInstructor);
                assertNotNull(responseClient);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertNotNull(loginRepository.findByAccount(user));
                assertTrue(loginRepository.findAll().size() == 2);

                // retrieve all logins through rest controller
                ResponseEntity<LoginListDto> responseList = client.getForEntity("/login/getAll", LoginListDto.class);
                // verify response is ok & list ok
                assertNotNull(responseList);
                assertEquals(HttpStatus.OK, responseList.getStatusCode());
                LoginListDto list = responseList.getBody();
                assertNotNull(list);
                assertEquals(2, list.getLogins().size());
                // verify list contains expected logins
                for (LoginResponseDto dto : list.getLogins()) {
                        if (dto.getAccountEmail().equals("example@mail.com")) {
                                assertEquals(dto.getAccountType(), "CLIENT");
                                assertEquals(loginRepository.findByAccount(user).getLoginId(), dto.getLoginId());
                        } else {
                                assertEquals(dto.getAccountEmail(), INSTRUCTOR_EMAIL);
                                assertEquals(dto.getAccountType(), LOGIN_TYPE);
                                assertEquals(loginRepository.findByAccount(instructor).getLoginId(), dto.getLoginId());
                        }
                }
        }

        @Test
        public void testIsStillLoggedIn() {
                // create Instructor, save to repo and confirm indeed exists in repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);
                // create goodTime (time before login expires) and bad time ( time after login
                // expires)
                Time goodTime = Time.valueOf("10:30:00");
                Time badTime = Time.valueOf("16:30:00");

                // create login request
                LoginRequestDto createRequest = new LoginRequestDto(LOGIN_TYPE, INSTRUCTOR_EMAIL, INSTRUCTOR_PASSWORD,
                                LOGIN_CURRENTTIME);
                // login through rest controller
                ResponseEntity<LoginResponseDto> createResponse = client.postForEntity("/login", createRequest,
                                LoginResponseDto.class);
                // confirm successfull login
                assertNotNull(createResponse);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                // build urls for both goodTime and badtime
                String urlGood = "/login/isStillLoggedIn/" + id + "/" + goodTime;
                String urlBad = "/login/isStillLoggedIn/" + id + "/" + badTime;
                // verify if isStillLoggedIn through rest controller
                ResponseEntity<Boolean> goodResponse = client.getForEntity(urlGood, Boolean.class);
                ResponseEntity<Boolean> badResponse = client.getForEntity(urlBad, Boolean.class);
                // verify if response is expected response
                assertNotNull(goodResponse);
                assertNotNull(badResponse);
                Boolean good = goodResponse.getBody();
                Boolean bad = badResponse.getBody();
                assertTrue(good);
                assertFalse(bad);
        }

        @Test
        public void testUpdateEndTime() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login request
                LoginRequestDto createRequest = new LoginRequestDto(LOGIN_TYPE, INSTRUCTOR_EMAIL, INSTRUCTOR_PASSWORD,
                                LOGIN_CURRENTTIME);
                // login through rest controller
                ResponseEntity<LoginResponseDto> createResponse = client.postForEntity("/login", createRequest,
                                LoginResponseDto.class);
                // confirm successfull login
                assertNotNull(createResponse);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                // create current time, and expected new_end_time once rest is called
                Time currentTime = Time.valueOf("12:30:00");
                Time new_endTime = Time.valueOf("15:30:00");
                // build url for updateEndTime request
                String url = "/login/updateEndtime/" + id + "/" + currentTime;
                // update endTime thorugh rest controller
                ResponseEntity<LoginResponseDto> response = client.exchange(url, HttpMethod.PUT, null,
                                LoginResponseDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                LoginResponseDto body = response.getBody();

                assertEquals(body.getAccountEmail(), INSTRUCTOR_EMAIL);
                assertEquals(body.getAccountType(), LOGIN_TYPE);
                Login login = loginRepository.findByLoginId(id);
                assertEquals(login.getEndTime(), new_endTime);
        }

        // all of the following tests are testing to ensure that our alternat endpoints
        // function as well.
        // They have the same logic as the previous of the same name
        @Test
        public void testLogIn2() {
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                assertNotNull(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);

                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login/", request,
                                LoginResponseDto.class);

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
        public void testLogOut2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                assertNotNull(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login for instructor through rest controller
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login/", request,
                                LoginResponseDto.class);
                assertNotNull(response);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);

                // request deletion of login in repo
                String url = "/logout/" + String.valueOf(loginRepository.findByAccount(instructor).getLoginId()) + "/";
                client.delete(url);

                // assert that login has been deleted from repo
                assertNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 0);
        }

        @Test
        public void testgetLoginByAccount2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login for instructor through rest controller
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login/", request,
                                LoginResponseDto.class);
                assertNotNull(response);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);

                String url = "/login/getByAccount/" + INSTRUCTOR_EMAIL + "/INSTRUCTOR/";
                ResponseEntity<LoginResponseDto> response2 = client.getForEntity(url, LoginResponseDto.class);

                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                LoginResponseDto response2Body = response2.getBody();
                assertNotNull(response2Body);
                assertEquals(INSTRUCTOR_EMAIL, response2Body.getAccountEmail());
                assertEquals(response2Body.getLoginId(), loginRepository.findByAccount(instructor).getLoginId());
                assertEquals(LOGIN_TYPE, response2Body.getAccountType());
        }

        @Test
        public void testGetLoginById2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login for instructor through rest controller
                LoginRequestDto request = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> response = client.postForEntity("/login/", request,
                                LoginResponseDto.class);
                assertNotNull(response);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                String url = "/login/getById/" + id + "/";
                ResponseEntity<LoginResponseDto> response2 = client.getForEntity(url, LoginResponseDto.class);

                assertNotNull(response2);
                assertEquals(HttpStatus.OK, response2.getStatusCode());
                LoginResponseDto response2Body = response2.getBody();
                assertNotNull(response2Body);
                assertEquals(INSTRUCTOR_EMAIL, response2Body.getAccountEmail());
                assertEquals(response2Body.getLoginId(), loginRepository.findByAccount(instructor).getLoginId());
                assertEquals(LOGIN_TYPE, response2Body.getAccountType());
        }

        @Test
        public void testGetAllLogins2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create client and save to repo
                Client user = new Client("example@mail.com", "firstname", "passWord4", "lastname", 0);
                clientRepository.save(user);
                user = clientRepository.findByEmail("example@mail.com");
                assertNotNull(user);

                // create login for instructor & client through rest controller
                LoginRequestDto requestInstructor = new LoginRequestDto(LOGIN_TYPE, LOGIN_EMAIL, LOGIN_PASSWORD,
                                LOGIN_CURRENTTIME);
                LoginRequestDto requestClient = new LoginRequestDto("CLIENT", "example@mail.com", "passWord4",
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> responseInstructor = client.postForEntity("/login/", requestInstructor,
                                LoginResponseDto.class);
                ResponseEntity<LoginResponseDto> responseClient = client.postForEntity("/login/", requestClient,
                                LoginResponseDto.class);

                assertNotNull(responseInstructor);
                assertNotNull(responseClient);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertNotNull(loginRepository.findByAccount(user));
                assertTrue(loginRepository.findAll().size() == 2);

                ResponseEntity<LoginListDto> responseList = client.getForEntity("/login/getAll/", LoginListDto.class);
                assertNotNull(responseList);
                assertEquals(HttpStatus.OK, responseList.getStatusCode());
                LoginListDto list = responseList.getBody();
                assertNotNull(list);
                assertEquals(2, list.getLogins().size());

                for (LoginResponseDto dto : list.getLogins()) {
                        if (dto.getAccountEmail().equals("example@mail.com")) {
                                assertEquals(dto.getAccountType(), "CLIENT");
                                assertEquals(loginRepository.findByAccount(user).getLoginId(), dto.getLoginId());
                        } else {
                                assertEquals(dto.getAccountEmail(), INSTRUCTOR_EMAIL);
                                assertEquals(dto.getAccountType(), LOGIN_TYPE);
                                assertEquals(loginRepository.findByAccount(instructor).getLoginId(), dto.getLoginId());
                        }
                }
        }

        @Test
        public void testIsStillLoggedIn2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);
                Time goodTime = Time.valueOf("10:30:00");
                Time badTime = Time.valueOf("16:30:00");

                // create login for instructor through rest controller
                LoginRequestDto createRequest = new LoginRequestDto(LOGIN_TYPE, INSTRUCTOR_EMAIL, INSTRUCTOR_PASSWORD,
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> createResponse = client.postForEntity("/login", createRequest,
                                LoginResponseDto.class);
                assertNotNull(createResponse);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                String urlGood = "/login/isStillLoggedIn/" + id + "/" + goodTime + "/";
                String urlBad = "/login/isStillLoggedIn/" + id + "/" + badTime + "/";
                ResponseEntity<Boolean> goodResponse = client.getForEntity(urlGood, Boolean.class);
                ResponseEntity<Boolean> badResponse = client.getForEntity(urlBad, Boolean.class);
                assertNotNull(goodResponse);
                assertNotNull(badResponse);
                Boolean good = goodResponse.getBody();
                Boolean bad = badResponse.getBody();
                assertTrue(good);
                assertFalse(bad);
        }

        @Test
        public void testUpdateEndTime2() {
                // create Instructor and save to repo
                Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FISTNAME, INSTRUCTOR_PASSWORD,
                                INSTRUCTOR_LASTNAME, INSTRUCTOR_VALID_ACCOUNTID);
                instructorRepository.save(instructor);
                instructor = instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL);
                assertNotNull(instructor);

                // create login for instructor through rest controller
                LoginRequestDto createRequest = new LoginRequestDto(LOGIN_TYPE, INSTRUCTOR_EMAIL, INSTRUCTOR_PASSWORD,
                                LOGIN_CURRENTTIME);
                ResponseEntity<LoginResponseDto> createResponse = client.postForEntity("/login/", createRequest,
                                LoginResponseDto.class);
                assertNotNull(createResponse);
                assertNotNull(loginRepository.findByAccount(instructor));
                assertTrue(loginRepository.findAll().size() == 1);
                int id = loginRepository.findByAccount(instructor).getLoginId();

                Time currentTime = Time.valueOf("12:30:00");
                Time new_endTime = Time.valueOf("15:30:00");
                String url = "/login/updateEndtime/" + id + "/" + currentTime + "/";

                ResponseEntity<LoginResponseDto> response = client.exchange(url, HttpMethod.PUT, null,
                                LoginResponseDto.class);

                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                LoginResponseDto body = response.getBody();

                assertEquals(body.getAccountEmail(), INSTRUCTOR_EMAIL);
                assertEquals(body.getAccountType(), LOGIN_TYPE);
                Login login = loginRepository.findByLoginId(id);
                assertEquals(login.getEndTime(), new_endTime);
        }

}
