package ca.mcgill.ecse321.SportPlus.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.checkerframework.dataflow.qual.TerminatesExecution;
import org.hibernate.annotations.TimeZoneStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.SportPlus;

public class ClientRepositoryTests {
    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    public void clearDatabse() {
        clientRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadClient() {
        // Create client.
        String aEmail = "john.doe@email.com";
        String aFirstName = "John";
        String aPassword = "pass";
        String aLastName = "Doe";
        int aAccountId = 1;
        SportPlus aSportPlus = new SportPlus();

        Client client = new Client(aEmail, aFirstName, aPassword, aLastName, aAccountId, aSportPlus);

        // Save client
        clientRepository.save(client);

        // read client from datyabse
        client = clientRepository.findClientByName(aLastName);

        // assert that not null and correct
        assertNotNull(client);
        assertEquals(client, client.getFirstName());
    }
}
