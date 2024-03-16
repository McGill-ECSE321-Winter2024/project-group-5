package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.service.ClientService;

@ExtendWith(MockitoExtension.class)
public class TestClientService {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private static final String CLIENT_EMAIL = "example@email.com";
    private static final String CLIENT_FISTNAME = "John";
    private static final String CLIENT_LASTNAME = "Doe";
    private static final String CLIENT_PASSWORD = "password123";
    private static final int CLIENT_ACCOUNTID = 2;
    private static final String NOT_CLIENT_EMAIL = "notemail@email.com";

    @SuppressWarnings("null")
    @BeforeEach
    public void setMockOutput() {
        lenient().when(clientRepository.findByEmail(CLIENT_EMAIL)).thenReturn(new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID));
        lenient().when(clientRepository.findByAccountId(CLIENT_ACCOUNTID)).thenReturn(new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID));
        lenient().when(clientRepository.findAll()).thenReturn(Arrays.asList(new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID)));
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
    }
    
    @Test
	public void testCreateClient() {
        String email = "newclient@email.com";
        String firstName = "Paul";
        String lastName = "Dmyt";
        String password = "Ro1234";
        
        Client client = clientService.createClient(email, firstName, password, lastName);
		
		assertNotNull(client);
		assertEquals(email, client.getEmail());
        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(password, client.getPassword());
        
        verify(clientRepository, times(1)).save(client);
	}

    @SuppressWarnings("null")
    @Test
    public void testUpdateClient() {
        String newPassword = "NewPass123";
        String newFirstName = "NewJohn";
        String newLastName = "NewDoe";
        
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        Client updatedClient = clientRepository.findByEmail(CLIENT_EMAIL);
        
        clientService.updateClientFirstName(CLIENT_EMAIL, newFirstName);
        clientService.updateClientLastName(CLIENT_EMAIL, newLastName);
        clientService.updateClientPassword(CLIENT_EMAIL, CLIENT_PASSWORD, newPassword);

        assertNotNull(updatedClient);
        assertEquals(client.getEmail(), updatedClient.getEmail());
        assertEquals(client.getAccountId(), updatedClient.getAccountId());
        assertNotEquals(client.getFirstName(), updatedClient.getFirstName());
        assertNotEquals(client.getLastName(), updatedClient.getLastName());
        assertNotEquals(client.getPassword(), updatedClient.getPassword());
        assertEquals(newPassword, updatedClient.getPassword());
        assertEquals(newFirstName, updatedClient.getFirstName());
        assertEquals(newLastName, updatedClient.getLastName());
        
        verify(clientRepository, times(3)).save(any(Client.class));
    }

    @Test
    public void testDeleteClient() {
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);

        clientService.deleteClient(client.getEmail());

        verify(clientRepository, times(1)).deleteByEmail(CLIENT_EMAIL);
    }

    @Test
	public void testGetExistingClient() {
		assertEquals(CLIENT_EMAIL, clientService.getClient(CLIENT_EMAIL).getEmail());
	}

	@Test
	public void testGetNonExistingClient() {
		assertNull(clientService.getClient(NOT_CLIENT_EMAIL));
	}

    @Test
    public void testReadClientById() {
        int accountId = CLIENT_ACCOUNTID;
        
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, accountId);
        Client searchClient = clientService.getClient(accountId);

        assertNotNull(searchClient);
        assertEquals(client.getAccountId(), searchClient.getAccountId());
        assertEquals(client.getEmail(), searchClient.getEmail());
        assertEquals(client.getFirstName(), searchClient.getFirstName());
        assertEquals(client.getLastName(), searchClient.getLastName());
        assertEquals(client.getPassword(), searchClient.getPassword());
    }

    @Test
    public void testGetAllClients() {
        Client client = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME, CLIENT_ACCOUNTID);
        List<Client> clients = Arrays.asList(client);
        List<Client> clientsFromRepo = clientService.getAllClients();

        assertNotNull(clientsFromRepo);
        assertEquals(clients, clientsFromRepo);
    }
    
}
