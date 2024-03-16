package ca.mcgill.ecse321.SportPlus.Service;

import java.lang.IllegalArgumentException;


import static org.mockito.Mockito.lenient;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @InjectMocks
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
    private static final Time startTime = Time.valueOf("10:30:00");
    private static final Time endTime = Time.valueOf("13:30:00");

    @BeforeEach
    public void setMockOutput() {
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD,OWNER_LASTNAME, OWNER_ACCOUNTID);
        Login login1 = new Login(LOGIN_ID1, startTime, endTime, instructor);
        Login login2 = new Login(LOGIN_ID2, startTime, endTime, client);
        Login login3 = new Login(LOGIN_ID3, startTime, endTime, owner);
        lenient().when(instructorRepository.findInstructorByEmail(INSTRUCTOR_EMAIL)).thenReturn(instructor);
        lenient().when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(client);
        lenient().when(ownerRepository.findByEmail(OWNER_EMAIL)).thenReturn(owner);
        lenient().when(loginRepository.findByLoginId(LOGIN_ID1)).thenReturn(login1);
        lenient().when(loginRepository.findByAccount(client)).thenReturn(login2);
        lenient().when(loginRepository.findByLoginId(LOGIN_ID3)).thenReturn(login3);
    }
    @Test
    public void testgetLoginFromAccount(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FIRSTNAME, CLIENT_PASSWORD,CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Instructor instructor = new Instructor(INSTRUCTOR_EMAIL, INSTRUCTOR_FIRSTNAME, INSTRUCTOR_PASSWORD,INSTRUCTOR_LASTNAME, INSTRUCTOR_ACCOUNTID);
        Login login =  new Login(LOGIN_ID2, startTime, endTime, client);
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
        Login login1 = new Login(LOGIN_ID1, startTime, endTime, instructor);
        Login foundLogin = loginService.getLoginFromId(LOGIN_ID1);

        assertThrows(IllegalArgumentException.class, () -> {
            loginService.getLoginFromId(LOGIN_ID2);
        }, "Login does not exist!");

        assertNotNull(foundLogin);
    
        assertEquals(foundLogin, login1);

    }
    // @Test
    // public void testDeleteByLoginId(){
        
    // }

   //deleteByLoginId
   //getAllLogins
   //createLogin
   //logIn
   //logOut
   //logOut
  // isStillLoggedIn
  //updateEndTime


    
}
