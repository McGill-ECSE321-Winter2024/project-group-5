package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.Login;

@SpringBootTest
public class LoginRepositoryTests {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loginRepository.deleteAll();
        clientRepository.deleteAll();
        instructorRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    public void testFindByLoginId() {
      
        Client client = new Client("example.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("example2.last@gmail.com", "aFirstName", "a5Password","aLastName", 0 );
        Owner owner = new Owner("example3.last@gmail.com", "aFirstName", "a5Password","aLastName", 0);
        // Save the client in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        Login loginClient = new Login(0, client);
        Login loginInstructor = new Login(1, instructor);
        Login loginOwner = new Login(2, owner);

        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        Login foundClient = loginRepository.findByLoginId(loginClient.getLoginId());
        Login foundInstructor = loginRepository.findByLoginId(loginInstructor.getLoginId());
        Login foundOwner = loginRepository.findByLoginId(loginOwner.getLoginId());

        assertThat(foundClient).isNotNull();
        assertThat(foundInstructor).isNotNull();
        assertThat(foundOwner).isNotNull();
        assertEquals(foundClient, loginClient);
        assertEquals(foundInstructor, loginInstructor);
        assertEquals(foundOwner, loginOwner);


    }
// Login findByLoginId(Integer loginId);

   
// findByEmail(String email);

// Void deleteByEmail(String email);

// List<Login> findAll();


    
}
