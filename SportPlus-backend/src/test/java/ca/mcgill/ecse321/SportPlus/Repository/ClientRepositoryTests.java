package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

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
    public void testFindByEmail() {
        // Setup the client
        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Client aClient = new Client(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        // Save the client in the database
        clientRepository.save(aClient);

        // Fetch the client from teh databse
        Client clientFromDb = clientRepository.findByEmail(aEmail);

        // Verify if the right client was fetched
        assertNotNull(clientFromDb);
        assertEquals(aEmail, clientFromDb.getEmail());
        assertEquals(aFirstName, clientFromDb.getFirstName());
        assertEquals(aPassword, clientFromDb.getPassword());
        assertEquals(aLastName, clientFromDb.getLastName());
    }

    @Test
    @Transactional
    public void testDeleteByEmail() {
        // Setup 2 clients
        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Client aClientToBeDeleted = new Client(aEmail, aFirstName, aPassword, aLastName, aAccountId);
        Client aClientToBeSaved = new Client("anEmail", "aFirstName", "aPassword", "aLastName", 2);

        // Save both clients to the database
        clientRepository.save(aClientToBeDeleted);
        clientRepository.save(aClientToBeSaved);

        // Delete only one client with email
        clientRepository.deleteByEmail(aEmail);

        // Verify if the client was removed
        assertThat(clientRepository.findByEmail(aEmail)).isNull();

        // Checks if aClientToBeSaved still there
        assertThat(clientRepository.findAll()).hasSize(1);
        Client remainingClient = clientRepository.findByEmail("anEmail");
        assertThat(remainingClient).isNotNull();
        assertThat(remainingClient.getEmail()).isEqualTo("anEmail");
    }

    @Test
    public void findAll() {
        // Given 4 clients
        Client client1 = new Client("test", "John", "123", "Fie", 0);
        Client client2 = new Client("test1", "John1", "123", "Fie1", 0);
        Client client3 = new Client("test2", "John2", "123", "Fie2", 0);
        Client client4 = new Client("tes2t", "John3", "123", "Fie3", 0);

        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);
        clientRepository.save(client4);

        List<Client> allClients = clientRepository.findAll();

        assertThat(allClients).hasSize(4);

        // Checks the emails
        assertThat(allClients).extracting(Client::getEmail)
                .containsExactlyInAnyOrder(client1.getEmail(), client2.getEmail(), client3.getEmail(),
                        client4.getEmail());

        // Checks the names
        assertThat(allClients).extracting(Client::getFirstName)
                .containsExactlyInAnyOrder(client1.getFirstName(), client2.getFirstName(), client3.getFirstName(),
                        client4.getFirstName());

        // Checks the last names
        assertThat(allClients).extracting(Client::getLastName)
                .containsExactlyInAnyOrder(client1.getLastName(), client2.getLastName(), client3.getLastName(),
                        client4.getLastName());

        // Checks accountIDs
        assertThat(allClients).extracting(Client::getAccountId)
                .containsExactlyInAnyOrder(client1.getAccountId(), client2.getAccountId(), client3.getAccountId(),
                        client4.getAccountId());

    }

    @Test
    @Transactional
    public void testFindByAccountId() {
        // Setup client
        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";

        Client client = new Client(aEmail, aFirstName, aPassword, aLastName, 0);

        // Save the client
        Client savedClient = clientRepository.save(client);
        int accountId = savedClient.getAccountId();

        // Fetch the client from the databse with the accountID
        Client clientFromDb = clientRepository.findByAccountId(accountId);

        // Verify if the client is right
        assertNotNull(clientFromDb);
        assertEquals(aEmail, clientFromDb.getEmail());
        assertEquals(aFirstName, clientFromDb.getFirstName());
        assertEquals(aPassword, clientFromDb.getPassword());
        assertEquals(aLastName, clientFromDb.getLastName());
        assertEquals(accountId, clientFromDb.getAccountId());
    }

}
