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
import static org.junit.jupiter.api.Assertions.assertNull;
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
import ca.mcgill.ecse321.SportPlus.dto.LoginRequestDto;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.ClientService;
import ca.mcgill.ecse321.SportPlus.service.InstructorService;
import ca.mcgill.ecse321.SportPlus.service.LoginService;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;

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

    @InjectMocks
    private InstructorService instructorService;

    @InjectMocks
    private OwnerService ownerService;

    @Mock
    private ClientService clientService;

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

    @BeforeEach
    public void setMockOutput() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD,OWNER_LASTNAME, OWNER_ACCOUNTID);
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
        lenient().when(clientService.getClient(CLIENT_EMAIL)).thenReturn(client);
    }
    @Test
    public void testGetLoginFromAccount(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Login login =  new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        Login foundLoginA = loginService.getLoginFromAccount(client);

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromAccount(null);
        }, "Account is null!");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromAccount(instructor);;
        }, "Login does not exist!");

        assertNotNull(foundLoginA);

        assertEquals(login, foundLoginA);
    }
    @Test
    public void testGetLoginFromId(){
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login foundLogin = loginService.getLoginFromId(LOGIN_ID1);

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromId(5);
        }, "Login does not exist!");

        assertNotNull(foundLogin);
    
        assertEquals(foundLogin, login1);

    }
    @Test
    public void testDeleteByLoginId(){
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
      

        loginService.deleteByLoginId(LOGIN_ID1);
        verify(loginRepository, times(1)).deleteByLoginId(LOGIN_ID1);
    }

    @Test
    public void testGetAllLogins(){
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD,OWNER_LASTNAME, OWNER_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login login2 = new Login(LOGIN_ID2, START_TIME, END_TIME, client);
        Login login3 = new Login(LOGIN_ID3, START_TIME, END_TIME, owner);

        List<Login> found = loginService.getAllLogins();
        assertNotNull(found);
        assertEquals(3, found.size());
        assertThat(found).contains(login1, login2, login3);
    }

    @Test
    public void testCreateLogin(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Login login = loginService.createLogin(LOGIN_ID1, START_TIME, END_TIME, client);

        assertNotNull(login);
        assertEquals(login.getAccount(), client);
        assertEquals(login.getEndTime(), END_TIME);
        assertEquals(login.getStartTime(), START_TIME);
        assertEquals(login.getLoginId(), LOGIN_ID1);
    }
    @Test
    public void testLogIn(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        LoginRequestDto good_request = new LoginRequestDto(0, CLIENT_EMAIL, START_TIME, "CLIENT");
        LoginRequestDto bad_request_type = new LoginRequestDto(0, CLIENT_EMAIL, START_TIME, "INSTRUCTOR");
        LoginRequestDto bad_request_owner = new LoginRequestDto(0, CLIENT_EMAIL, START_TIME, "OWNER");

        Login goodLogin = loginService.logIn("CLIENT", CLIENT_EMAIL, CLIENT_PASSWORD, START_TIME);

        assertNotNull(goodLogin);
        assertEquals(goodLogin.getAccount(), client);
        assertEquals(goodLogin.getEndTime(), END_TIME);
        assertEquals(goodLogin.getStartTime(), START_TIME);
        assertEquals(goodLogin.getLoginId(), 0);//because id is @Generated, loginService puts 0 as idInput
        //because this is simulating the database, and not actually using it, here the id is not generated
        //in reality, it wont be 0. Additionally, when creating the Login, we dont care of the loginId that comes with the request.
        //it is useful for other aspects of the code

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("CLIENT", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "Wrong Password!");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("INSTRUCTOR", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "Account of Type " + bad_request_type.getAccountType()+ " with given email does not exist.");

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.logIn("OWNER", CLIENT_EMAIL, "wrongpassworD4", START_TIME);
        }, "This is not an Owner email.");
    }

    @Test
    public void testLogOut(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        LoginRequestDto clientRequest = new LoginRequestDto(0, CLIENT_EMAIL, START_TIME, "CLIENT");

        loginService.logIn("CLIENT", CLIENT_EMAIL, CLIENT_PASSWORD, START_TIME);

        loginService.logOut(0);
        verify(loginRepository, times(1)).deleteByLoginId(0);
    }

    @Test
    public void testIsStillLoggedIn(){
        //create 2 logins, 
        //create 2 requests and  1 of the request will have currentTime> than endTime
        // the other wil have endTime > currenttime
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);

        Login login1 = new Login(LOGIN_ID1, START_TIME, END_TIME, instructor);
        Login login2 = new Login(LOGIN_ID2, START_TIME, END_TIME, client);

        Time good_time = Time.valueOf("11:30:00");
        Time bad_time = Time.valueOf("15:30:00");

        assertTrue(loginService.isStillLoggedIn(LOGIN_ID1, good_time));
        assertFalse(loginService.isStillLoggedIn(LOGIN_ID2, bad_time));
        
    }

    @Test
    public void testUpdateEndTime(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Login login = new Login(LOGIN_ID2, START_TIME, END_TIME, client);

        Time current_time = Time.valueOf("11:30:00");
        Time new_end_time = Time.valueOf("14:30:00");

        Login login2 = loginService.updateEndTime(LOGIN_ID2,current_time);
        assertNotNull(login2);
        assertEquals(login2.getEndTime(), new_end_time);
    }
    
}
