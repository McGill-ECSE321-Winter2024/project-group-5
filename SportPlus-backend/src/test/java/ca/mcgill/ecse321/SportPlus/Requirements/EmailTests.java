package ca.mcgill.ecse321.SportPlus.Requirements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClientRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.RegistrationResponseDto;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;

// To properly verify whether email have been sent, please sign into the CLIENT_EMAIL
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmailTests {

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
        // Clear the DB
        instructorRepository.deleteAll();
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        classTypeRepository.deleteAll();
        clientRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    // Initialize variables
    private static final String CLIENT_EMAIL = "sportplus.noreplyemail@gmail.com";
    private static final String CLIENT_FISTNAME = "SportPlus";
    private static final String CLIENT_LASTNAME = "Client";
    private static final String CLIENT_PASSWORD = "Password123";
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

    /*
     * In order to test whether an email is sent to the client after account
     * creation, client should login into the CLIENT_EMAIL and check whether an
     * email has been receieved.
     * 
     * This has been tested and the email is receieved within 10 seconds of running
     * the test.
     * 
     * NFR2
     */
    @Test
    public void testEmailForClientAccountRegistration() {

        // Create a request
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        // Get the response
        ResponseEntity<ClientResponseDto> response = restTemplate.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        // Validate the response
        assertNotNull(response);
    }

    /*
     * In order to test whether an email is sent to the client after registration to
     * a specific class, client should login into the CLIENT_EMAIL and check whether
     * an email has been receieved.
     * 
     * This has been tested and the email is receieved within 10 seconds of running
     * the test.
     * 
     * NFR1
     */
    @Test
    public void testEmailForClientRegistrationSpecificClass() {

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

        // Building the registration request DTO
        RegistrationRequestDto request = new RegistrationRequestDto(specificClass, client);
        assertEquals(CLIENT_EMAIL, request.getClient().getEmail());

        // Building the registration response DTO
        ResponseEntity<RegistrationResponseDto> response = restTemplate.postForEntity("/registrations/create/", request,
                RegistrationResponseDto.class);
        assertNotNull(response);
    }

}
