package ca.mcgill.ecse321.SportPlus.Service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.service.ClientService;

import java.util.List;
import java.util.ArrayList;


public class TestClientService {

    @Mock
    private ClientRepository clientDao;

    @InjectMocks
    private ClientService clientService;

    private final String EMAIL = "example@gmail.com";
    private final String FIRSTNAME = "Hiccup Horrendus";
    private final String LASTNAME = "Haddock III";
    private final String PASSWORD = "ILoveToothlessMoreThanAstrid";
    private final String ACCOUNTID = "0";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(clientDao.findByEmail(anyString())).thenAnswer( (InvocationOnMock invocation) -> {
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
        lenient().when(clientDao.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            Client client = new Client();
            client.setEmail(EMAIL);
            client.setFirstName(FIRSTNAME);
            client.setLastName(LASTNAME);
            client.setPassword(PASSWORD);
            List<Client> clientList = new ArrayList<>();
            clientList.add(client);
            return clientList;
        });
    }
    
}
