package ca.mcgill.ecse321.SportPlus.Service;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.service.RegistrationService;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;

@ExtendWith(MockitoExtension.class)
public class TestRegistrationService {

    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private SpecificClassRepository specificClassRepository;
    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private RegistrationService registrationService;

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
    private static SpecificClass aSpecificClass = new SpecificClass(SPECIFICCLASS_DATE, SPECIFICCLASS_STARTTIME, SPECIFICCLASS_ENDTIME, SPECIFICCLASS_ID, aClassType, "NewOne");

    private static Registration REGISTRATION = new Registration(REGISTRATION_ID, aSpecificClass, aClient);

    private static String SPECIFICCLASS_NAME = "NewOne";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(registrationRepository.findByRegId(REGISTRATION_ID)).thenReturn(REGISTRATION);
        lenient().when(specificClassRepository.findBySessionId(SPECIFICCLASS_ID)).thenReturn(aSpecificClass);
        lenient().when(specificClassRepository.findByDateAndStartTime(SPECIFICCLASS_DATE,SPECIFICCLASS_STARTTIME)).thenReturn(aSpecificClass);
        lenient().when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(aClient);
        lenient().when(registrationRepository.findByClient(aClient)).thenReturn(Arrays.asList(REGISTRATION));
        lenient().when(registrationRepository.findBySpecificClass(aSpecificClass)).thenReturn(Arrays.asList(REGISTRATION));
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(registrationRepository.save(any(Registration.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(specificClassRepository.findByName(SPECIFICCLASS_NAME)).thenReturn(aSpecificClass);
    }
        

    
    @Test
    public void testCreateRegistration(){

        Registration registration = registrationService.createRegistration(SPECIFICCLASS_NAME, CLIENT_EMAIL);

        assertNotNull(registration);
        assertEquals(REGISTRATION_ID, registration.getRegId());
        assertEquals(SPECIFICCLASS_ID, registration.getSpecificClass().getSessionId());
        assertEquals(CLIENT_EMAIL, registration.getClient().getEmail());
    }

    @Test
    public void testGetRegistrationById() {
        Registration registration = registrationService.getByRegistrationId(REGISTRATION_ID);

        assertNotNull(registration);
        assertEquals(registration.getClient(), aClient);
        assertEquals(registration.getRegId(), REGISTRATION_ID);
        assertEquals(registration.getSpecificClass(), aSpecificClass);
    }

    @Test
    public void testDeleteRegistrationById(){
        registrationService.deleteByRegistrationId(REGISTRATION.getRegId());
        verify(registrationRepository, times(1)).deleteByRegId(REGISTRATION_ID);
    }

    @Test
    public void testGetByClient(){

        List<Registration> registrations = Arrays.asList(REGISTRATION);
        List<Registration> registrationsFromRepo = registrationService.getByClient(CLIENT_EMAIL);
        assertNotNull(registrationsFromRepo);
        assertEquals(registrations, registrationsFromRepo);

    }

    @Test
    public void testDeleteRegistrationByClient(){
        registrationService.deleteByClient(aClient.getEmail());
        verify(registrationRepository, times(1)).deleteByClient(aClient);;
    }

    @Test
    public void testGetBySpecificClass(){

        List<Registration> registrations = Arrays.asList(REGISTRATION);
        List<Registration> registrationsFromRepo = registrationService.getBySpecificClass(SPECIFICCLASS_ID);
        assertNotNull(registrationsFromRepo);
        assertEquals(registrations, registrationsFromRepo);

    }
    @Test
    public void testDeleteRegistrationBySpecificClass(){
        registrationService.deleteBySpecificClass(aSpecificClass.getSessionId());
        verify(registrationRepository, times(1)).deleteBySpecificClass(aSpecificClass);
    }
    
}
