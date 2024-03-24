package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationListDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationRequestDto;
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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        instructorRepository.deleteAll();
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        clientRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private static final String CLIENT_EMAIL = "example@email.com";
    private static final String CLIENT_FISTNAME = "John";
    private static final String CLIENT_LASTNAME = "Doe";
    private static final String CLIENT_PASSWORD = "password123";
    private static final int CLIENT_ACCOUNTID = 2;

    private static final int SPECIFICCLASS_ID = 3;
    private static final Date SPECIFICCLASS_DATE = Date.valueOf("2024-04-16");
    private static final Time SPECIFICCLASS_STARTTIME = Time.valueOf("11:00:00");
    private static final Time SPECIFICCLASS_ENDTIME = Time.valueOf("12:00:00");

    private static final int CLASS_TYPE_ID =6;
    private static final String CLASS_TYPE_NAME = "Yoga";
    private static final String CLASS_TYPE_DESCRIPTION = "Description";

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 3;

    @Test
    public void testFindRegistrationByClient(){

        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);

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
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, specificClass.getDate());
        }
    }

    @Test
    public void testFindRegistrationByClient2(){
         Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);


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
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
        }
    }

    @Test
    public void testFindRegistrationBySpecificClass(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);


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
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, registration.getSpecificClass().getDate());
        }

    }

    @Test
    public void testFindRegistrationBySpecificClass2(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        Registration registration2 = new Registration(reg_id, specificClass, client);


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
            assertEquals(CLASS_TYPE_NAME, registration.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registration.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registration.getSpecificClass().getStartTime());
            assertEquals(SPECIFICCLASS_DATE, registration.getSpecificClass().getDate());
        }

    }

    @Test 
    public void testFindRegistrationByRegId(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        registrationRepository.save(registration1);

        reg_id = registration1.getRegId();

        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id);
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationResponseDto registrationResponse = response.getBody();
        assertNotNull(registrationResponse);

        assertNotNull(registrationResponse);
            assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
            assertEquals(CLASS_TYPE_NAME, registrationResponse.getSpecificClass().getClassType().getName());
            assertEquals(SPECIFICCLASS_ENDTIME, registrationResponse.getSpecificClass().getEndTime());
            assertEquals(SPECIFICCLASS_STARTTIME, registrationResponse.getSpecificClass().getStartTime());
    }

    @Test 
    public void testFindRegistrationByRegId2(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        registrationRepository.save(registration1);

        reg_id = registration1.getRegId();

        String url = "/registrations/getByRegistrationId/" + String.valueOf(reg_id) + "/";
        ResponseEntity<RegistrationResponseDto> response = restTemplate.getForEntity(url, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationResponseDto registrationResponse = response.getBody();
        assertNotNull(registrationResponse);

        assertEquals(CLIENT_EMAIL, registrationResponse.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, registrationResponse.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, registrationResponse.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, registrationResponse.getSpecificClass().getStartTime());
    }

    @Test
    public void testDeleteRegistrationByClient(){
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
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
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        registrationRepository.save(registration1);

        String url = "/registrations/getByClient/" + String.valueOf(CLIENT_EMAIL)+ "/";
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
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
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
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);
        
        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
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
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        specificClass = specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME);
        client = clientRepository.findByEmail(CLIENT_EMAIL);
        int reg_id = 0;
        Registration registration1 = new Registration(reg_id, specificClass, client);
        registrationRepository.save(registration1);

        String url = "/registrations/getBySpecificClass/" + String.valueOf(SPECIFICCLASS_ID) + "/";
        ResponseEntity<RegistrationListDto> response = restTemplate.getForEntity(url, RegistrationListDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationListDto registrationList = response.getBody();
        assertNotNull(registrationList);
    }

    @Test
    public void testCreateRegistration(){

        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType aClassType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, true, owner);
        SpecificClass specificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType);
        
        clientRepository.save(client);
        ownerRepository.save(owner);
        classTypeRepository.save(aClassType);
        specificClassRepository.save(specificClass);

        RegistrationRequestDto request = new RegistrationRequestDto(specificClass, client);
        assertEquals(CLIENT_EMAIL, request.getClient().getEmail());
        assertEquals(SPECIFICCLASS_DATE, request.getSpecificClass().getDate());

        ResponseEntity<RegistrationResponseDto> response = restTemplate.postForEntity("/registrations/create", request, RegistrationResponseDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        RegistrationResponseDto createdRegistration = response.getBody();
        assertNotNull(createdRegistration);
        assertEquals(CLIENT_EMAIL, createdRegistration.getClient().getEmail());
        assertEquals(CLASS_TYPE_NAME, createdRegistration.getSpecificClass().getClassType().getName());
        assertEquals(SPECIFICCLASS_ENDTIME, createdRegistration.getSpecificClass().getEndTime());
        assertEquals(SPECIFICCLASS_STARTTIME, createdRegistration.getSpecificClass().getStartTime());
        assertEquals(SPECIFICCLASS_DATE, createdRegistration.getSpecificClass().getDate());
    }



    
}
