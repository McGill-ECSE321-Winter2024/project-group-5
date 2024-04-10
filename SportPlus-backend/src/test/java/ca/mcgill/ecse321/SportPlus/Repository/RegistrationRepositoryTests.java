package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.ClassType;

@SpringBootTest
public class RegistrationRepositoryTests {

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
    public void testFindByRegId() {
        // Given a registration
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        clientRepository.save(client);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, "newName");
        specificClassRepository.save(specificClass);

        Registration registration = new Registration(0, specificClass, client);
        registrationRepository.save(registration);

        // When the registration is fetched by Id
        Registration foundRegistration = registrationRepository.findByRegId(registration.getRegId());

        // Then the registration should match the initail registration
        assertNotNull(foundRegistration, "Registration should not be null");
        assertEquals(specificClass.getSessionId(), foundRegistration.getSpecificClass().getSessionId(),
                "SpecificClass should match");
        assertEquals(client.getAccountId(), foundRegistration.getClient().getAccountId(), "Client should match");
        assertEquals("John", foundRegistration.getClient().getFirstName(), "Client should match the name");
        assertEquals(registration.getSpecificClass().getClassType(),
                foundRegistration.getSpecificClass().getClassType(),
                "Type should match the name");
    }

    @Test
    @Transactional
    public void testDeleteByRegId() {
        // Setup 2 registrations
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        clientRepository.save(client);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, "hi");
        specificClassRepository.save(specificClass);

        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis, null);
        specificClassRepository.save(specificClass2);

        Registration registration1 = new Registration(0, specificClass, client);
        registrationRepository.save(registration1);

        Registration registration2 = new Registration(0, specificClass2, client);
        registrationRepository.save(registration2);

        // Delete only the second registration
        registrationRepository.deleteByRegId(registration2.getRegId());

        // Then, registration2 Should not be there
        assertThat(registrationRepository.findByRegId(registration2.getRegId())).isNull();

        // registration1 should still be there
        Registration foundRegistration = registrationRepository.findByRegId(registration1.getRegId());
        assertNotNull(foundRegistration, "Registration should not be null");
        assertEquals(specificClass.getSessionId(), foundRegistration.getSpecificClass().getSessionId(),
                "SpecificClass should match");
        assertEquals(client.getAccountId(), foundRegistration.getClient().getAccountId(), "Client should match");
        assertEquals("John", foundRegistration.getClient().getFirstName(), "Client should match the name");
        assertEquals(registration1.getSpecificClass().getClassType(),
                foundRegistration.getSpecificClass().getClassType(),
                "Type should match the name");
    }

    @Test
    @Transactional
    public void testFinBySpecificClass() {
        // Given two registrations
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        Client client2 = new Client("tes2t@email.com", "Paul", "321", "Doe", 0);
        Client client3 = new Client("tes3t@email.com", "Bob", "213", "Doe", 0);
        clientRepository.save(client);
        clientRepository.save(client2);
        clientRepository.save(client3);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis, null);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);

        Registration registration = new Registration(0, specificClass, client);
        Registration registration2 = new Registration(0, specificClass, client2);

        // Different class
        Registration registration3 = new Registration(0, specificClass2, client3);

        registrationRepository.save(registration);
        registrationRepository.save(registration2);
        registrationRepository.save(registration3);

        // When the registrations are feteched from database by SpecificClass
        List<Registration> foundRegistrations = registrationRepository.findBySpecificClass(specificClass);

        // Should have the 2 registrations not 3.
        assertEquals(2, foundRegistrations.size());

        // The found registrations should contain registration and registration2
        assertThat(foundRegistrations).contains(registration, registration2);
    }

    @Test
    @Transactional
    public void testDeleteBySpecificClass() {
        // Setup 2 specifc classes
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        clientRepository.save(client);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis, null);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);

        Registration registration = new Registration(0, specificClass, client);
        Registration registration2 = new Registration(0, specificClass2, client);
        registrationRepository.save(registration);
        registrationRepository.save(registration2);

        // Delete only 1 registration that is specificClass2
        registrationRepository.deleteBySpecificClass(specificClass2);

        // Then, registration Should not be there
        assertThat(registrationRepository.findBySpecificClass(specificClass2)).isEmpty();

        // registration should still be there
        List<Registration> foundRegistrations = registrationRepository.findBySpecificClass(specificClass);
        assertEquals(1, foundRegistrations.size());
        assertThat(foundRegistrations).contains(registration);
    }

    @Test
    @Transactional
    public void testFindByClient() {
        // Given 3 registrations
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client john = new Client("test@email.com", "John", "123", "Doe", 0);
        Client client2 = new Client("test2@email.com", "Bob", "321", "Doe", 0);
        clientRepository.save(john);
        clientRepository.save(client2);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, null);
        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis, null);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);

        // 3 Registrations, client(John) has 2 registrations
        Registration registration = new Registration(0, specificClass, john);
        Registration registrationSameClient = new Registration(0, specificClass2, john);
        Registration registration3 = new Registration(0, specificClass, client2);

        registrationRepository.save(registration);
        registrationRepository.save(registrationSameClient);
        registrationRepository.save(registration3);

        // Same client should have multiple regsitrations
        List<Registration> foundRegistrations = registrationRepository.findByClient(john);

        // Should have the 2 registrations John registered for, not 3.
        assertEquals(2, foundRegistrations.size());

        // Then the found registrations should contain registration and
        // registrationSameClient
        assertThat(foundRegistrations).contains(registration, registrationSameClient);
    }

    @Test
    @Transactional
    public void testDeleteByClient() {
        // Setup 2 registrations with diffferent clients
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);
        ownerRepository.save(owner);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        Client clientToBeDeleted = new Client("tes2t@email.com", "Paul", "321", "Bob", 0);
        clientRepository.save(client);
        clientRepository.save(clientToBeDeleted);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        classTypeRepository.save(yoga);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga, null);
        specificClassRepository.save(specificClass);

        Registration registration = new Registration(0, specificClass, client);
        Registration registrationToBeDeleted = new Registration(0, specificClass, clientToBeDeleted);
        registrationRepository.save(registration);
        registrationRepository.save(registrationToBeDeleted);

        // Delete only 1 registration
        registrationRepository.deleteByClient(clientToBeDeleted);

        // Then, registration with clientToBeDeleted should be empty
        assertThat(registrationRepository.findByClient(clientToBeDeleted)).isEmpty();

        // Second registration with client should still be there
        List<Registration> foundRegistrations = registrationRepository.findByClient(client);
        assertEquals(1, foundRegistrations.size());
        assertThat(foundRegistrations).contains(registration);
    }

}
