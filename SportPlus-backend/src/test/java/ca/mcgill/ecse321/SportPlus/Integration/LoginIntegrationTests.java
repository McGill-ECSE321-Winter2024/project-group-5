package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Time;
import java.util.TimeZone;

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
import ca.mcgill.ecse321.SportPlus.model.Login;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.Client;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LoginIntegrationTests {
    @Autowired
    private TestRestTemplate webClient;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private InstructorRepository instructorRepository;

     @Autowired
    private OwnerRepository ownerRepository;

    private static final Time START_TIME = Time.valueOf("11:00:00");
    private static final Time END_TIME = Time.valueOf("14:00:00");



    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loginRepository.deleteAll();
        clientRepository.deleteAll();
        instructorRepository.deleteAll();
        ownerRepository.deleteAll();
    }
    @BeforeEach
    public void setup() {

        //TimeZone.setDefault(TimeZone.getTimeZone("EDT"));

        Owner owner = new Owner("email@owner.com", "Johm", "password", "theOwner", 0);
        ownerRepository.save(owner);
     
        Instructor instructor = new Instructor("instructor@Sportplus.com", "firstName", "password4Name", "lastName",0);
        instructorRepository.save(instructor);
    
    }

    @Test
    void testLogin(){
    //     Client client = new Client("Aelin.Ashryver.Galathynius@gmail.com", "Aelin","haha4IbeatMaeve", "Galathynius",0);
    //     clientRepository.save(client);

    //     LoginRequestDto loginRequest = new LoginRequestDto(0,"Aelin.Ashryver.Galathynius@gmail.com", START_TIME, "CLIENT");

    //     Login test = new Login(0,START_TIME, END_TIME, client);
    //     loginRepository.save(test);

    //     String url = "/login/haha4IbeatMaeve";

    //     ResponseEntity<LoginResponseDto> responseDto = webClient.postForEntity(url, loginRequest, LoginResponseDto.class);
    //     Login login = loginRepository.findByAccount(client);

    //     assertNotNull(login);
    //     assertEquals(login.getEndTime(), END_TIME);
    //     assertEquals(login.getStartTime(), START_TIME);
    //     assertEquals(login.getAccount(), client);

    //     assertNotNull(responseDto);
    //     // assertEquals(HttpStatus.CREATED, responseDto.getStatusCode());
    //     LoginResponseDto response = responseDto.getBody();
    //     assertTrue(login.getLoginId() > 0);
    //     assertEquals(response.getAccountType(),"CLIENT");
    //     assertEquals(response.getAccountEmail(),"Aelin.Ashryver.Galathynius@gmail.com");

    }
}
