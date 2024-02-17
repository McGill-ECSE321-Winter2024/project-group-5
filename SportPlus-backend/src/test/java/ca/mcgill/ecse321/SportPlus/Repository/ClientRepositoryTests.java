package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        clientRepository.save(aClient);

        Client clientFromDb = clientRepository.findClientByEmail(aEmail);

        assertNotNull(clientFromDb);
        assertEquals(aEmail, clientFromDb.getEmail());
        assertEquals(aFirstName, clientFromDb.getFirstName());
        assertEquals(aPassword, clientFromDb.getPassword());
        assertEquals(aLastName, clientFromDb.getLastName());
    }

    @Test
    @Transactional
    public void testDeleteClientByEmail() {

        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";
        int aAccountId = 0;

        Client aClientToBeDeleted = new Client(aEmail, aFirstName, aPassword, aLastName, aAccountId);

        Client aClientToBeSaved = new Client("anEmail", "aFirstName", "aPassword", "aLastName", 2);

        clientRepository.save(aClientToBeDeleted);
        clientRepository.save(aClientToBeSaved);

        clientRepository.deleteClientByEmail(aEmail);

        assertThat(clientRepository.findClientByEmail(aEmail)).isNull();

        // Checks if aClientToBeSaved still there
        assertThat(clientRepository.findAll()).hasSize(1);
        Client remainingClient = clientRepository.findClientByEmail("anEmail");
        assertThat(remainingClient).isNotNull();
        assertThat(remainingClient.getEmail()).isEqualTo("anEmail"); // Further verify the details if needed
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
        String aEmail = "example.last@gmail.com";
        String aFirstName = "Example";
        String aPassword = "1234567890";
        String aLastName = "Last";

        Client client = new Client(aEmail, aFirstName, aPassword, aLastName, 0);
        Client savedClient = clientRepository.save(client);
        int accountId = savedClient.getAccountId();

        Client clientFromDb = clientRepository.findByAccountId(accountId);

        assertNotNull(clientFromDb);
        assertEquals(aEmail, clientFromDb.getEmail());
        assertEquals(aFirstName, clientFromDb.getFirstName());
        assertEquals(aPassword, clientFromDb.getPassword());
        assertEquals(aLastName, clientFromDb.getLastName());
        assertEquals(accountId, clientFromDb.getAccountId());
    }

}