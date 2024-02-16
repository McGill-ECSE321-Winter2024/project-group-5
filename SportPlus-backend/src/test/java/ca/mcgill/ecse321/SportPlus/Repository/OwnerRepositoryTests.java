package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

//import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;

@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;

    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    //@Test
    public void testPersistAndLoadOwner() {
        // Create owner.
        String firstname = "John";
        Integer userId = 42;
        String password = "SecretPass";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        Owner owner = new Owner(email, firstname, userId, password, lastName);

        // Save owner
        ownerRepository.save(owner);

        // Read owner from database.
        Owner loadedOwner = ownerRepository.findByEmail(email);

        // Assert that owner is not null and has correct attributes.
        assertNotNull(loadedOwner);
        assertEquals(firstname, loadedOwner.getFirstname());
        assertEquals(userId, loadedOwner.getUserId());
        assertEquals(password, loadedOwner.getPassword());
        assertEquals(lastName, loadedOwner.getLastName());
        assertEquals(email, loadedOwner.getEmail());
    }
}