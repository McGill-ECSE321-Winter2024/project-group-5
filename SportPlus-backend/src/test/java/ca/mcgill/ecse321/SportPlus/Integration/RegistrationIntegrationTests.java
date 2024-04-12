package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.Date;
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
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
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

    // Constants for test data
    private static final String CLIENT_EMAIL = "example@email.com";
    private static final String CLIENT_FISTNAME = "John";
    private static final String CLIENT_LASTNAME = "Doe";
    private static final String CLIENT_PASSWORD = "password123";
    private static final int CLIENT_ACCOUNTID = 2;

    private static final int SPECIFICCLASS_ID = 3;
    private static final Date SPECIFICCLASS_DATE = Date.valueOf("2024-04-16");
    private static final Time SPECIFICCLASS_STARTTIME = Time.valueOf("11:00:00");
    private static final Time SPECIFICCLASS_ENDTIME = Time.valueOf("12:00:00");

    private static final int CLASS_TYPE_ID = 6;
    private static final String CLASS_TYPE_NAME = "Yoga";
    private static final String CLASS_TYPE_DESCRIPTION = "Description";

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 3;

    private static final String SPECIFICCLASS_NAME = "YogaonMonday";

    // Test method for finding registration by client email
    @Test
    public void testFindRegistrationByClient() {

        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating two registrations associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);

        // Saving registrations to the repository
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL);
        // Sending a GET request to retrieve registrations associated with the client
        // email
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        // Asserting that the response is not null and has a status code of OK
        assertNotNull(response);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        // Iterating over each registration in the list and performing assertions
        for (RegistrationResponseDto registration : registrationList.getRegistrations()) {
            // Assertions
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, specificClass.getDate());
        }
    }

    // Second test method for finding registration by client email
    @Test
    public void testFindRegistrationByClient2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating two registrations associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);

        // Saving registrations to the repository
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL) + "/";
        // Sending a GET request to retrieve registrations associated with the client
        // email
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        // Asserting that the response is not null and has a status code of OK
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
        for (RegistrationResponseDto registration : registrationList.getRegistrations()) {
            // Assertions
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
        }
    }

    @Test
    public void testFindRegistrationBySpecificClass() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating two registrations associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);

        // Saving registrations to the repository
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
        for (RegistrationResponseDto registration : registrationList.getRegistrations()) {
            // Assertions
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, registration.getSpecificClass().getDate());
        }

    }

    @Test
    public void testFindRegistrationBySpecificClass2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating two registrations associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);

        // Saving registrations to the repository
        registrationRepository.save(registration1);
        registrationRepository.save(registration2);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);

        for (RegistrationResponseDto registration : registrationList.getRegistrations()) {
            // Assertions
            assertNotNull(registration);
            assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, registration.getSpecificClass().getDate());
        }

    }

    @Test
    public void testFindRegistrationByRegId() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);
        reg_id = registration1.getRegId();

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id);
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url,
                RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration from the response body
        RegistrationResponseDto registrationResponse = response.getBody();

        // Assertions
        assertNotNull(registrationResponse);
        assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, registrationResponse.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, registrationResponse.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, registrationResponse.getSpecificClass().getStartTime());
    }

    @Test
    public void testFindRegistrationByRegId2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);
        reg_id = registration1.getRegId();

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id) + "/";
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url,
                RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration from the response body
        RegistrationResponseDto registrationResponse = response.getBody();

        // Assertions
        assertNotNull(registrationResponse);
        assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, registrationResponse.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, registrationResponse.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, registrationResponse.getSpecificClass().getStartTime());
    }

    @Test
    public void testDeleteRegistrationByClient() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
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
    public void testDeleteRegistrationByClient2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
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
    public void testDeleteRegistrationBySpecificClass() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID);
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
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
    public void testDeleteRegistrationBySpecificClass2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration list from the response body
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
    public void testDeleteRegistrationByRegId() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);
        reg_id = registration1.getRegId();

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id);
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url,
                RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration from the response body
        RegistrationResponseDto registrationResponse = response.getBody();

        // Assertions
        assertNotNull(registrationResponse);

        reg_id = registrationResponse.getRegId();
        String urlToDelete = "/registrations/deleteByRegistrationId/" + String.valueOf(reg_id);
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationResponse = response.getBody();
        assertNull(registrationResponse);

    }

    @Test
    public void testDeleteRegistrationByRegId2() {
        // Test data setup
        // Creating instances of Client, Owner, ClassType, and SpecificClass
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);

        // Saving entities to repositories
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        // Retrieving saved entities from repositories
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        // Creating registration associated with the client and specific class
        Registration registration1 = new Registration(reg_id, specificClass, client);
        // Saving registration to the repository
        registrationRepository.save(registration1);
        reg_id = registration1.getRegId();

        // Test execution
        // Building the URL for the GET request
        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id) + "/";
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url,
                RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Extracting the registration from the response body
        RegistrationResponseDto registrationResponse = response.getBody();

        // Assertions
        assertNotNull(registrationResponse);

        reg_id = registrationResponse.getRegId();
        String urlToDelete = "/registrations/deleteByRegistrationId/" + String.valueOf(reg_id) + "/";
        restTemplate.delete(urlToDelete);

        response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        registrationResponse = response.getBody();
        assertNull(registrationResponse);
    }

    // Test method to create a registration
    @Test
    public void testCreateRegistration() {
        // Test data setup
        // Creating instances of Owner, ClassType, SpecificClass, and Client and saving
        // entities to repositories
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ownerRepository.save(owner);
        owner = ownerRepository.findByEmail(OWNER_EMAIL);

        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        clientRepository.save(client);
        assertNotNull(owner);

        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        classTypeRepository.save(aClassType);
        aClassType = classTypeRepository.findByName(CLASS_TYPE_NAME);
        assertNotNull(aClassType);

        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);
        specificClassRepository.save(specificClass);
        assertNotNull(specificClass);

        client = clientRepository.findByEmail(CLIENT_EMAIL);
        assertNotNull(client);

        // Building the registration request URL
        String url = "/registrations/create/" + specificClass.getSessionId()+"/" + CLIENT_EMAIL;

        // Building the registration response DTO
        ResponseEntity<RegistrationResponseDto> response = restTemplate.postForEntity(url,null,RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Extracting the registration from the response body
        RegistrationResponseDto createdRegistration = response.getBody();
        assertNotNull(createdRegistration);
        assertEquals(CLIENT_EMAIL, createdRegistration.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, createdRegistration.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, createdRegistration.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, createdRegistration.getSpecificClass().getStartTime());
    }

    // Test method to create a registration
    @Test
    public void testCreateRegistration2() {
        // Test data setup
        // Creating instances of Owner, ClassType, SpecificClass, and Client and saving
        // entities to repositories
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ownerRepository.save(owner);
        owner = ownerRepository.findByEmail(OWNER_EMAIL);

        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        clientRepository.save(client);
        assertNotNull(owner);

        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        classTypeRepository.save(aClassType);
        aClassType = classTypeRepository.findByName(CLASS_TYPE_NAME);
        assertNotNull(aClassType);

        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME,
                SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, SPECIFICCLASS_NAME);
        specificClassRepository.save(specificClass);
        assertNotNull(specificClass);

        client = clientRepository.findByEmail(CLIENT_EMAIL);
        assertNotNull(client);

        // Building the registration request URL
        String url = "/registrations/create/" + specificClass.getSessionId()+"/" + CLIENT_EMAIL;

        // Building the registration response DTO
        ResponseEntity<RegistrationResponseDto> response = restTemplate.postForEntity(url,null,RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Extracting the registration from the response body
        RegistrationResponseDto createdRegistration = response.getBody();
        assertNotNull(createdRegistration);
        assertEquals(CLIENT_EMAIL, createdRegistration.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, createdRegistration.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, createdRegistration.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, createdRegistration.getSpecificClass().getStartTime());
    }

}
