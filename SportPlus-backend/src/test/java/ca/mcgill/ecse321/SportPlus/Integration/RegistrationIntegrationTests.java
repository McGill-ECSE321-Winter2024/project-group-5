package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
import java.sql.Time;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegistrationRepository registrationRepository; 

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
    }

    private static final int REGISTRATION_ID = 0;
    private static final String CLIENT_EMAIL = "example@email.com";
    private static final String CLIENT_FISTNAME = "John";
    private static final String CLIENT_LASTNAME = "Doe";
    private static final String CLIENT_PASSWORD = "password123";
    private static final int CLIENT_ACCOUNTID = 2;
    private static Client aClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);

    private static final int SPECIFICCLASS_ID = 3;
    private static final Date SPECIFICCLASS_DATE = new Date(1805400000L);
    private static final Time SPECIFICCLASS_STARTTIME = new Time(1805400000000L);
    private static final Time SPECIFICCLASS_ENDTIME = new Time(1805403600000L);

    private static final int CLASS_TYPE_ID =6;
    private static final String CLASS_TYPE_NAME = "Yoga";
    private static final String CLASS_TYPE_DESCRIPTION = "Description";

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 3;

    private static Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
    private static ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
    private static SpecificClass aSpecificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);

    //private static Registration REGISTRATION = new Registration(REGISTRATION_ID, aSpecificClass, aClient);


    @Test
    public void testFindRegistrationByClient(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        Registration registration2 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);
        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
        for(RegistrationResponseDto registration : registrationList.getRegistrations()){
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(SPECIFICCLASS_ID, registration.getSpecificClass().getId());

        }
    }

    @Test
    public void testFindRegistrationByClient2(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        Registration registration2 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);

        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL) + "/";

        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
        for(RegistrationResponseDto registration : registrationList.getRegistrations()){
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(SPECIFICCLASS_ID, registration.getSpecificClass().getId());
        }
    }

    @Test
    public void testFindRegistrationBySpecificClass(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        Registration registration2 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);

        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        for(RegistrationResponseDto registration : registrationList.getRegistrations()){
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(SPECIFICCLASS_ID, registration.getSpecificClass().getId());
        }

    }

    @Test
    public void testFindRegistrationBySpecificClass2(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        Registration registration2 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);

        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
        
        for(RegistrationResponseDto registration : registrationList.getRegistrations()){
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(SPECIFICCLASS_ID, registration.getSpecificClass().getId());
        }

    }

    @Test 
    public void testFindRegistrationByRegId(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);


        String url = "/registrations/getByRegistrationId/" + String.valueOf(REGISTRATION_ID);
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationResponseDto registrationResponse = response.getBody();
        assertNotNull(registrationResponse);

        assertNotNull(registrationResponse);
        assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
        assertEquals(SPECIFICCLASS_ID, registrationResponse.getSpecificClass().getId());


    }

    @Test 
    public void testFindRegistrationByRegId2(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getByRegistrationId/" + String.valueOf(REGISTRATION_ID) + "/";
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationResponseDto registrationResponse = response.getBody();
        assertNotNull(registrationResponse);

        assertNotNull(registrationResponse);
        assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
        assertEquals(SPECIFICCLASS_ID, registrationResponse.getSpecificClass().getId());
    }

    @Test
    public void testDeleteRegistrationByClient(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        String urlToDelete = "/registrations/deleteByClient/" + String.valueOf(CLIENT_EMAIL);
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationList = response.getBody();
        assertTrue(registrationList.getRegistrations().size() == 0);

    }

    @Test
    public void testDeleteRegistrationByClient2(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        String urlToDelete = "/registrations/deleteByClient/" + String.valueOf(CLIENT_EMAIL) + "/";
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationList = response.getBody();
        assertTrue(registrationList.getRegistrations().size() == 0);

    }

    @Test
    public void testDeleteRegistrationBySpecificClass(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        String urlToDelete = "/registrations/deleteBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID);
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationList = response.getBody();
        assertTrue(registrationList.getRegistrations().size() == 0);
    }

    @Test
    public void testDeleteRegistrationBySpecificClass2(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        String urlToDelete = "/registrations/deleteBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationList = response.getBody();
        assertTrue(registrationList.getRegistrations().size() == 0);
    }

    @Test 
    public void testDeleteRegistrationByRegId(){
        Registration registration1 = new Registration(REGISTRATION_ID, aSpecificClass, aClient);
        registrationRepository.save(registration1);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
    }

    



    
}
