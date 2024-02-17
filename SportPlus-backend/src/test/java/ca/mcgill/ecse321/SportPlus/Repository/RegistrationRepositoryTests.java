package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

}
