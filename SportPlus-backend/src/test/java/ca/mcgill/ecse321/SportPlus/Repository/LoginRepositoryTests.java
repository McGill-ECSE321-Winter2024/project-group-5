package ca.mcgill.ecse321.SportPlus.Repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.Login;

@SpringBootTest
public class LoginRepositoryTests {

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

    @Test
    @Transactional
    public void testFindByLoginId() {
        //create users
        Client client = new Client("example.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("example2.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Owner owner = new Owner("example3.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        // Save users in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        //login users
        Login loginClient = new Login(0, null, null, client);
        Login loginInstructor = new Login(0, null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);
        //save logins in database
        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);
        //find logins
        Login foundClient = loginRepository.findByLoginId(loginClient.getLoginId());
        Login foundInstructor = loginRepository.findByLoginId(loginInstructor.getLoginId());
        Login foundOwner = loginRepository.findByLoginId(loginOwner.getLoginId());
        //verify that found logins are the expected logins
        assertThat(foundClient).isNotNull();
        assertThat(foundInstructor).isNotNull();
        assertThat(foundOwner).isNotNull();
        assertEquals(foundClient, loginClient);
        assertEquals(foundInstructor, loginInstructor);
        assertEquals(foundOwner, loginOwner);
    }

    @Test
    @Transactional
    public void findByAccount() {
        //create users
        Client client = new Client("jerry.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("tom.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Owner owner = new Owner("bugs.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        // Save users in the database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        //create logins
        Login loginClient = new Login(0, null, null, client);
        Login loginInstructor = new Login(0, null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);
        //save logins in database           
        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        //find logins by account
        Login foundClient = loginRepository.findByAccount(client);
        Login foundInstructor = loginRepository.findByAccount(instructor);
        Login foundOwner = loginRepository.findByAccount(owner);
        //verify that found logins are the expected logins
        assertThat(foundClient).isNotNull();
        assertThat(foundInstructor).isNotNull();
        assertThat(foundOwner).isNotNull();
        assertEquals(foundClient, loginClient);
        assertEquals(foundInstructor, loginInstructor);
        assertEquals(foundOwner, loginOwner);
    }

    @Test
    @Transactional
    public void testFindAll() {
        //create users
        Client client = new Client("example.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Client client2 = new Client("example4.lastname@gmail.com", "firstname", "lastName", "56Upssword", 0);
        Instructor instructor = new Instructor("example2.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Owner owner = new Owner("example3.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
       // Save users in the database
        clientRepository.save(client);
        clientRepository.save(client2);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        //create logins
        Login loginClient = new Login(0, null, null, client);
        Login loginClient2 = new Login(0, null, null, client2);
        Login loginInstructor = new Login(0, null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);
        //save logins in database   
        loginRepository.save(loginClient);
        loginRepository.save(loginClient2);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);

        //find all loggins and verify that found loggins are expected loggins
        List<Login> found = loginRepository.findAll();
        assertNotNull(found);
        assertEquals(4, found.size());
        assertThat(found).contains(loginClient, loginClient2, loginInstructor, loginOwner);
    }

    @Test
    @Transactional
    public void testDeleteByAccount() {
        //create users
        Client client = new Client("jerry.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Instructor instructor = new Instructor("tom.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        Owner owner = new Owner("bugs.last@gmail.com", "aFirstName", "a5Password", "aLastName", 0);
        //Save users in database
        clientRepository.save(client);
        instructorRepository.save(instructor);
        ownerRepository.save(owner);
        //create loggins
        Login loginClient = new Login(0, null, null, client);
        Login loginInstructor = new Login(0, null, null, instructor);
        Login loginOwner = new Login(0, null, null, owner);
        //save logins in database
        loginRepository.save(loginClient);
        loginRepository.save(loginInstructor);
        loginRepository.save(loginOwner);
        // find all logins and verify that all logins are expected logins
        List<Login> found = loginRepository.findAll();
        assertNotNull(found);
        assertEquals(found.size(), 3);
        assertThat(found).contains(loginClient, loginInstructor, loginOwner);
        //delete client, and find all logins again. verify that client was successfully deleted, and is not in found logins
        loginRepository.deleteByLoginId(loginClient.getLoginId());
        found = loginRepository.findAll();
        assertEquals(found.size(), 2);
        assertThat(found).contains(loginOwner, loginInstructor);
        //delete instructor, and find all logins again. verify that instructor was successfully deleted, and is not in found logins
        loginRepository.deleteByLoginId(loginInstructor.getLoginId());
        found = loginRepository.findAll();
        assertEquals(found.size(), 1);
        assertThat(found).contains(loginOwner);
    }

}
