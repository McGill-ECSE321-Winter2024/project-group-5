package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Owner;

@SpringBootTest
public class OwnerRepositoryTests {

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
    public void testFindownerByEmail() {
        // Given an owner
        String aEmail = "owner@sportplus.com";
        String aFirstName = "Owner";
        String aPassword = "123321";
        String aLastName = "Owner Lastname";
        int aAccountId = 0;

        Owner owner = new Owner(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        ownerRepository.save(owner);

        // When owner is fetched from databse by the email
        Owner ownerFromDb = ownerRepository.findByEmail(aEmail);

        // Then the owner should correspond to the initail owner
        assertNotNull(ownerFromDb);
        assertEquals(aEmail, ownerFromDb.getEmail());
        assertEquals(aFirstName, ownerFromDb.getFirstName());
        assertEquals(aPassword, ownerFromDb.getPassword());
        assertEquals(aLastName, ownerFromDb.getLastName());
    }

    @Test
    @Transactional
    public void testFindByAccountId() {
        // Given an owner
        String aEmail = "owner@sportplus.com";
        String aFirstName = "Owner";
        String aPassword = "123321";
        String aLastName = "Owner Lastname";
        int aAccountId = 0;

        Owner owner = new Owner(aEmail, aFirstName, aPassword, aLastName, aAccountId);
        ownerRepository.save(owner);
        Owner ownerFromDb = ownerRepository.findByEmail(aEmail);
        int accountId = ownerFromDb.getAccountId();

        // When owner is fetched from databse by the AccountId
        Owner savedFromDb = ownerRepository.findByAccountId(accountId);

        // Then the owner should correspond to the initail owner
        assertNotNull(savedFromDb);
        assertEquals(aEmail, savedFromDb.getEmail());
        assertEquals(aFirstName, savedFromDb.getFirstName());
        assertEquals(aPassword, savedFromDb.getPassword());
        assertEquals(aLastName, savedFromDb.getLastName());
        assertEquals(accountId, savedFromDb.getAccountId());
    }

}
