package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void testFindByLoginId() {
       
        Client client = new Client("example.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("example2.last@gmail.com", "aFirstName", "a5Password","aLastName", 0 );
        Owner owner = new Owner("example3.last@gmail.com", "aFirstName", "a5Password","aLastName", 0);
        // Save the client in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        Login loginClient = new Login(0,null, null, client);
        Login loginInstructor = new Login(0,null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);

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

    @Test
    @Transactional
    public void findByAccount(){
        Client client = new Client("jerry.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("tom.last@gmail.com", "aFirstName", "a5Password","aLastName", 0 );
        Owner owner = new Owner("bugs.last@gmail.com", "aFirstName", "a5Password","aLastName", 0);
        // Save the client in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        Login loginClient = new Login(0,null, null, client);
        Login loginInstructor = new Login(0,null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);

        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        Login foundClient = loginRepository.findByAccount(client);
        Login foundInstructor = loginRepository.findByAccount(instructor);
        Login foundOwner = loginRepository.findByAccount(owner);

        assertThat(foundClient).isNotNull();
        assertThat(foundInstructor).isNotNull();
        assertThat(foundOwner).isNotNull();
        assertEquals(foundClient, loginClient);
        assertEquals(foundInstructor, loginInstructor);
        assertEquals(foundOwner, loginOwner);

    }
    @Test
    @Transactional 
    public void testFindAll(){
        Client client = new Client("example.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Client client2 = new Client("example4.lastname@gmail.com", "firstname", "lastName", "56Upssword", 0);
        Instructor instructor = new Instructor("example2.last@gmail.com", "aFirstName", "a5Password","aLastName", 0 );
        Owner owner = new Owner("example3.last@gmail.com", "aFirstName", "a5Password","aLastName", 0);
        // Save the client in the database
        clientRepository.save(client);
        clientRepository.save(client2);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        Login loginClient = new Login(0,null, null, client);
        Login loginClient2 = new Login(0, null, null, client2);
        Login loginInstructor = new Login(0,null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);

        loginRepository.save(loginClient);
        loginRepository.save(loginClient2);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        List<Login> found = loginRepository.findAll();

        assertNotNull(found);
        assertEquals(4, found.size());
        assertThat(found).contains(loginClient, loginClient2, loginInstructor, loginOwner);
    }


    @Test
    @Transactional
    public void testDeleteByAccount(){
        Client client = new Client("jerry.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("tom.last@gmail.com", "aFirstName", "a5Password","aLastName", 0 );
        Owner owner = new Owner("bugs.last@gmail.com", "aFirstName", "a5Password","aLastName", 0);
        // Save the client in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        Login loginClient = new Login(0,null, null, client);
        Login loginInstructor = new Login(0,null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);

        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        List<Login> found = loginRepository.findAll();
        assertEquals(found.size(), 3);
        assertThat(found).contains(loginClient, loginInstructor, loginOwner);

        loginRepository.deleteByLoginId(loginClient.getLoginId());
        found = loginRepository.findAll();
        assertEquals(found.size(), 2);
        assertThat(found).contains(loginOwner, loginInstructor);

        loginRepository.deleteByLoginId(loginInstructor.getLoginId());
        found = loginRepository.findAll();
        assertEquals(found.size(), 1);
        assertThat(found).contains(loginOwner);
    }
      
}
