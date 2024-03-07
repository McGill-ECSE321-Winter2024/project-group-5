package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    private final String EMAIL = "example@gmail.com";
    private final String FIRSTNAME = "Hiccup Horrendus";
    private final String LASTNAME = "Haddock III";
    private final String PASSWORD = "ILoveToothlessMoreThanAstrid";
    private final String ACCOUNTID = "0";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(clientRepository.findByEmail(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
            if(invocation.getArgument(0).equals(ACCOUNTID)) {
                Client client = new Client();
                client.setEmail(EMAIL);
                client.setFirstName(FIRSTNAME);
                client.setLastName(LASTNAME);
                client.setPassword(PASSWORD);
                return client;
            } else {
               return null;
            }
        });
		Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(clientRepository.save(any(Client.class))).thenAnswer(returnParameterAsAnswer);
    }

    @SuppressWarnings("null")
    @Test
	public void testCreateInstructor() {
        String email = "newClient@email.com";
        String firstName = "Jake";
        String lastName = "Travolta";
        String password = "dropWisdom";

		assertEquals(0, clientService.getAllClients().size());

        Client client = null;
		try {
            client = clientService.createClient(email, password, firstName, lastName);
		} catch (IllegalArgumentException e) {
			fail();
		}
		assertNotNull(client);
		assertEquals(email, client.getEmail());
        assertEquals(firstName, client.getFirstName());
        assertEquals(lastName, client.getLastName());
        assertEquals(password, client.getPassword());
        verify(clientRepository, times(1)).save(client);
	}
    //TODO : 
    // @Test
    // public void testUpdateClientFirstName(){
    //     String email = "newClient@email.com";
    //     String firstName = "Zachary";

    // }
    // @Test
    // public void testUpdateClientLastName(){
    //     String email = "newClient@email.com";
    //     String lastName = "Hillander";

    // }
    // @Test
    // public void testUpdateClientPassword(){
    //     String email = "newClient@email.com";
    //     String password = "passworD4";

    // }
    // @Test
    // public void testUpdateClientEmail(){
    //     int accountId = 333;
    //     String email = "this@email.com";
    // }



    
}
