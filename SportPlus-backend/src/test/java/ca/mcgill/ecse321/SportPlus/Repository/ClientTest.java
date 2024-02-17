package ca.mcgill.ecse321.SportPlus.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.SportPlus.model.Client;

@SpringBootTest
public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    public void clearDatabase() {
        clientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadClient() {
        // Create client.
        String firstname = "Muffin Man";
        Integer userId = 40;
        String password = "123 Drury Lane";
        String lastName = "";
        String email = "muffin.man@example.com"; // Provide a valid email

        Client client = new Client(email, firstname, password, lastName, userId);

        // Save client
        clientRepository.save(client);

        // Read client from database.
        Client loadedClient = clientRepository.findClientByEmail(email);

        // Assert that client is not null and has correct attributes.
        assertNotNull(loadedClient);
        assertEquals(firstname, loadedClient.getFirstname());
        assertEquals(userId, loadedClient.getUserId());
        assertEquals(password, loadedClient.getPassword());
        assertEquals(lastName, loadedClient.getLastName());
        assertEquals(email, loadedClient.getEmail());
    }
}
