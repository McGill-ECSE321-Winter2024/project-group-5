package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;

@SpringBootTest
public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        clientRepository.deleteAll();
    }

    @Test
    public void testFindClientByEmail() {
        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Client aClient = new Client(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        aClient = clientRepository.save(aClient);

        Client clientFromDb = clientRepository.findClientByEmail(aEmail);

        assertNotNull(clientFromDb);
        assertEquals(aEmail, clientFromDb.getEmail());
        assertEquals(aFirstName, clientFromDb.getFirstName());
        assertEquals(aPassword, clientFromDb.getPassword());
        assertEquals(aLastName, clientFromDb.getLastName());
    }
}