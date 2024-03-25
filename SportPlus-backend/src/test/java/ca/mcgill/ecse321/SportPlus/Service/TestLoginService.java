package ca.mcgill.ecse321.SportPlus.Service;

import java.lang.IllegalArgumentException;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.LoginService;

@ExtendWith(MockitoExtension.class)
public class TestLoginService {

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginService loginService;

    private static final String INSTRUCTOR_EMAIL = "instructor@sportplus.com";
    private static final String INSTRUCTOR_FIRSTNAME = "Hiccup Horrendous";
    private static final String INSTRUCTOR_LASTNAME = "Haddock III";
    private static final String INSTRUCTOR_PASSWORD = "passworD123";
    private static final int INSTRUCTOR_ACCOUNTID = 2;

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "Astrid";
    private static final String OWNER_LASTNAME = "Haddock";
    private static final String OWNER_PASSWORD = "passworD123";
    private static final int OWNER_ACCOUNTID = 3;

    private static final String CLIENT_EMAIL = "instructor@email.com";
    private static final String CLIENT_FIRSTNAME = "John";
    private static final String CLIENT_LASTNAME = "Cena";
    private static final String CLIENT_PASSWORD = "passworD123";
    private static final int CLIENT_ACCOUNTID = 4;

    private static final int LOGIN_ID1 = 1;
    private static final int LOGIN_ID2 = 2;
    private static final int LOGIN_ID3 = 3;
    private static final Time START_TIME = Time.valueOf("10:30:00");
    private static final Time END_TIME = Time.valueOf("13:30:00");

    /**
     * Sets up mock outputs before each test.
     */
    @BeforeEach
    public void setMockOutput() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login login2 = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        Login login3 = new Login(LOGIN_ID3, START_TIME, END_TIME, owner);
        lenient().when(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL)).thenReturn(instructor);
        lenient().when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(client);
        lenient().when(ownerRepository.findByEmail(OWNER_EMAIL)).thenReturn(owner);
        lenient().when(loginRepository.findByLoginId(LOGIN_ID1)).thenReturn(login1);
        lenient().when(loginRepository.findByLoginId(LOGIN_ID2)).thenReturn(login2);
        lenient().when(loginRepository.findByAccount(client)).thenReturn(login2);
        lenient().when(loginRepository.findByLoginId(LOGIN_ID3)).thenReturn(login3);
        lenient().when(loginRepository.findAll()).thenReturn(Arrays.asList(login1, login2, login3));
    }

    @Test
    public void testGetLoginFromAccount() {
        //set up of test by creating client and instructor, and logging in  client
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        clientRepository.save(client);
        instructorRepository.save(instructor);
        Login login = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        loginRepository.save(login);

        //verify coorect number of invocations in repositories
        verify(clientRepository, times(1)).save(client);
        verify(instructorRepository, times(1)).save(instructor);
        verify(loginRepository, times(1)).save(login);

        //verify correct functionality when login exists and is ok
        Login foundLoginA = loginService.getLoginFromAccount(client);
        assertNotNull(foundLoginA);
        assertEquals(login, foundLoginA);

        //verify if expected exceptions are thown with problematic inputs
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromAccount(null);
        }, "Account is null!");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromAccount(instructor);
            ;
        }, "Login does not exist!");

    }

    @Test
    public void testGetLoginFromId() {
        //set up by creating instructor, and logging in instructor
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        instructorRepository.save(instructor);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        loginRepository.save(login1);

        //verify coorect number of invocations in repositories
        verify(instructorRepository, times(1)).save(instructor);
        verify(loginRepository, times(1)).save(login1);

        //verify correct functionality when login exists and is ok
        Login foundLogin = loginService.getLoginFromId(LOGIN_ID1);
        assertNotNull(foundLogin);
        assertEquals(foundLogin, login1);

        //verify if expected exception is thown with problematic input
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromId(5);
        }, "Login does not exist!");
    }

    @Test
    public void testGetAllLogins() {
        //set up by creating insturctr, client an owner, and logging in each of them
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login login2 = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        Login login3 = new Login(LOGIN_ID3, START_TIME, END_TIME, owner);

        //save to repository
        instructorRepository.save(instructor);
        clientRepository.save(client);
        ownerRepository.save(owner);
        loginRepository.save(login1);
        loginRepository.save(login2);
        loginRepository.save(login3);

        //verify correct number of invocations in repositories
        verify(clientRepository, times(1)).save(client);
        verify(instructorRepository, times(1)).save(instructor);
        verify(ownerRepository, times(1)).save(owner);
        verify(loginRepository, times(1)).save(login1);
        verify(loginRepository, times(1)).save(login2);
        verify(loginRepository, times(1)).save(login3);

        //get all logins and verify result
        List<Login> found = loginService.getAllLogins();
        assertNotNull(found);
        assertEquals(3, found.size());
        assertThat(found).contains(login1, login2, login3);
    }

    @Test
    public void testLogIn() {
        //set up test by creating a client
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        //attempt to log in client through the service
        Login goodLogin = loginService.logIn("CLIENT", CLIENT_EMAIL, CLIENT_PASSWORD, START_TIME);

        //verify if client is logged in as expected
        assertNotNull(goodLogin);
        assertEquals(goodLogin.getAccount(), client);
        assertEquals(goodLogin.getEndTime(), END_TIME);
        assertEquals(goodLogin.getStartTime(), START_TIME);
        assertEquals(goodLogin.getLoginId(), 0);// because id is @Generated, loginService puts 0 as idInput
        // because this is simulating the database, and not actually using it, here the
        // id is not generated because we are not actually accessing the database.
        // In reality, it wont be 0. 

        //verify if expected exceptions are thown with problematic inputs
        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("CLIENT", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "Wrong Password!");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("INSTRUCTOR", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "Account of Type " + "INSTRUCTOR" + " with given email does not exist.");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("OWNER", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "This is not an Owner email.");
    }

    @Test
    public void testLogOut() {
        //create client
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        clientRepository.save(client);
        //verify correct number of invocations in repository
        verify(clientRepository, times(1)).save(client);

        //login client -- we dont verify this, as previous test shows it to behave adequately
        loginService.logIn("CLIENT", CLIENT_EMAIL, CLIENT_PASSWORD, START_TIME);

        //logout and verify behaviour
        loginService.logOut(0);
        verify(loginRepository, times(1)).deleteByLoginId(0);
    }

    @Test
    public void testIsStillLoggedIn() {
        // create 2 clients and 2 logins,
        // create 2 requests and 1 of the request will have currentTime> than endTime
        // the other wil have endTime > currenttime
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,
                INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        instructorRepository.save(instructor);
        clientRepository.save(client);

        //verify correct number of invocations in repositories
        verify(clientRepository, times(1)).save(client);
        verify(instructorRepository, times(1)).save(instructor);

        //create and save logins
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login login2 = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        loginRepository.save(login1);
        loginRepository.save(login2);

        //verify correct number of invocations in repositories
        verify(loginRepository, times(1)).save(login1);
        verify(loginRepository, times(1)).save(login2);
    
        //create good time (before endTime of login) and bad time (past endTime of login)
        Time good_time = Time.valueOf("11:30:00");
        Time bad_time = Time.valueOf("15:30:00");

        //verify expected result
        assertTrue(loginService.isStillLoggedIn(LOGIN_ID1, good_time));
        assertFalse(loginService.isStillLoggedIn(LOGIN_ID2, bad_time));
    }

    @Test
    public void testUpdateEndTime() {
        //create client and login client
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        clientRepository.save(client);
        verify(clientRepository, times(1)).save(client);

        Login login = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        loginRepository.save(login);
        verify(loginRepository, times(1)).save(login);

        //create currentTime (is meant to simulate the time at which a cient makes a request)
        //create new-end_time (is meant to be new updated end time)
        Time current_time = Time.valueOf("11:30:00");
        Time new_end_time = Time.valueOf("14:30:00");

        //attempt to update endtime and verify outcome
        Login login2 = loginService.updateEndTime(LOGIN_ID2, current_time);
        assertNotNull(login2);
        assertEquals(login2.getEndTime(), new_end_time);
    }

}
