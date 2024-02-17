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

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        registrationRepository.deleteAll();
        specificClassRepository.deleteAll();
        clientRepository.deleteAll();
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    // Registration findByRegId(int regId);
    @Test
    @Transactional
    public void testFindByRegId() {

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga);

        Registration registration = new Registration(0, specificClass, client);

        registrationRepository.save(registration);
        specificClassRepository.save(specificClass);
        clientRepository.save(client);
        classTypeRepository.save(yoga);
        ownerRepository.save(owner);

        Registration foundRegistration = registrationRepository.findByRegId(registration.getRegId());

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
    public void testFinBySpecificClass() {
        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        Client client = new Client("test@email.com", "John", "123", "Doe", 0);
        Client client2 = new Client("tes2t@email.com", "Paul", "321", "Doe", 0);
        Client client3 = new Client("tes3t@email.com", "Bob", "213", "Doe", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga);
        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis);

        Registration registration = new Registration(0, specificClass, client);
        Registration registration2 = new Registration(0, specificClass, client2);

        // Different class
        Registration registration3 = new Registration(0, specificClass2, client3);

        registrationRepository.save(registration);
        registrationRepository.save(registration2);
        registrationRepository.save(registration3);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        clientRepository.save(client);
        clientRepository.save(client2);
        clientRepository.save(client3);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);
        ownerRepository.save(owner);

        List<Registration> foundRegistrations = registrationRepository.findBySpecificClass(specificClass);

        // Should have the 2 registrations not 3.
        assertEquals(2, foundRegistrations.size());

        assertThat(foundRegistrations).contains(registration, registration2);

    }

    @Test
    @Transactional
    public void testFinByClient() {

        Owner owner = new Owner("Owner@email.com", "Owner", "123", "owner last anme", 0);

        Client john = new Client("test@email.com", "John", "123", "Doe", 0);
        Client client2 = new Client("test2@email.com", "Bob", "321", "Doe", 0);

        ClassType yoga = new ClassType("yoga", "cool class", 0, true, owner);
        ClassType tennis = new ClassType("tennis", "tennis class", 0, true, owner);

        SpecificClass specificClass = new SpecificClass(null, null, null, 0, yoga);
        SpecificClass specificClass2 = new SpecificClass(null, null, null, 0, tennis);

        // 3 Registrations, client(John) has 2 registrations
        Registration registration = new Registration(0, specificClass, john);
        Registration registrationSameClient = new Registration(0, specificClass2, john);
        Registration registration3 = new Registration(0, specificClass, client2);

        registrationRepository.save(registration);
        registrationRepository.save(registrationSameClient);
        registrationRepository.save(registration3);
        specificClassRepository.save(specificClass);
        specificClassRepository.save(specificClass2);
        clientRepository.save(john);
        clientRepository.save(client2);
        classTypeRepository.save(yoga);
        classTypeRepository.save(tennis);
        ownerRepository.save(owner);

        // Same client should have multiple regsitrations
        List<Registration> foundRegistrations = registrationRepository.findByClient(john);

        // Should have the 2 registrations John registered for, not 3.
        assertEquals(2, foundRegistrations.size());

        assertThat(foundRegistrations).contains(registration, registrationSameClient);

    }

}
