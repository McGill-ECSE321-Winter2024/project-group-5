package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClientListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Client;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClientIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        clientRepository.deleteAll();
    }

    private static final String CLIENT_EMAIL = "example@email.com";
    private static final String CLIENT_FISTNAME = "John";
    private static final String CLIENT_LASTNAME = "Doe";
    private static final String CLIENT_PASSWORD = "Password123";

    private int CLIENT_VALID_ACCOUNTID;

    @Test
    public void testFindClientByEmail() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);

        String url = "/clients/getByEmail/" + String.valueOf(CLIENT_EMAIL);

        ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientResponseDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        assertTrue(clientResponse.getAccountId() > 0);
        assertEquals(CLIENT_EMAIL, clientResponse.getEmail());
        assertEquals(CLIENT_FISTNAME, clientResponse.getFirstName());
        assertEquals(CLIENT_LASTNAME, clientResponse.getLastName());
    }

    @Test
    public void testFindClientByEmail2() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);

        String url = "/clients/getByEmail/" + String.valueOf(CLIENT_EMAIL) + "/";

        ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientResponseDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        assertTrue(clientResponse.getAccountId() > 0);
        assertEquals(CLIENT_EMAIL, clientResponse.getEmail());
        assertEquals(CLIENT_FISTNAME, clientResponse.getFirstName());
        assertEquals(CLIENT_LASTNAME, clientResponse.getLastName());
    }

    @Test
    public void testFindClientByAccountId() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);
        int validId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String url = "/clients/getById/" + String.valueOf(validId);

        ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientResponseDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        assertEquals(validId, clientResponse.getAccountId());
        assertEquals(CLIENT_EMAIL, clientResponse.getEmail());
        assertEquals(CLIENT_FISTNAME, clientResponse.getFirstName());
        assertEquals(CLIENT_LASTNAME, clientResponse.getLastName());
    }

    @Test
    public void testFindClientByAccountId2() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);
        int validId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String url = "/clients/getById/" + String.valueOf(validId) + "/";

        ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientResponseDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        assertEquals(validId, clientResponse.getAccountId());
        assertEquals(CLIENT_EMAIL, clientResponse.getEmail());
        assertEquals(CLIENT_FISTNAME, clientResponse.getFirstName());
        assertEquals(CLIENT_LASTNAME, clientResponse.getLastName());
    }

    @Test
    public void testGetAllClients() {
        Client client1 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        Client client2 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(client1);
        clientRepository.save(client2);

        String url = "/clients/all";

        ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientListDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        for (ClientResponseDto client : clientResponse.getClients()) {
            assertNotNull(client);
            assertTrue(client.getAccountId() > 0);
            assertEquals(CLIENT_EMAIL, client.getEmail());
            assertEquals(CLIENT_FISTNAME, client.getFirstName());
            assertEquals(CLIENT_LASTNAME, client.getLastName());
        }
    }

    @Test
    public void testGetAllClients2() {
        Client client1 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        Client client2 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(client1);
        clientRepository.save(client2);

        String url = "/clients/all/";

        ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientListDto clientResponse = response.getBody();
        assertNotNull(clientResponse);
        for (ClientResponseDto client : clientResponse.getClients()) {
            assertNotNull(client);
            assertTrue(client.getAccountId() > 0);
            assertEquals(CLIENT_EMAIL, client.getEmail());
            assertEquals(CLIENT_FISTNAME, client.getFirstName());
            assertEquals(CLIENT_LASTNAME, client.getLastName());
        }
    }

    @Test
    public void testDeleteClientByEmail() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);

        String url = "/clients/all";

        ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientListDto clientResponse = response.getBody();
        assertTrue(clientResponse.getClients().size() == 1);
        assertTrue(clientResponse.getClients().get(0).getEmail().equals(CLIENT_EMAIL));

        String urlToDelete = "/clients/delete/" + CLIENT_EMAIL;

        client.delete(urlToDelete);

        response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        clientResponse = response.getBody();
        assertTrue(clientResponse.getClients().size() == 0);
    }

    @Test
    public void testDeleteClientByEmail2() {
        Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                CLIENT_VALID_ACCOUNTID);
        clientRepository.save(newClient);

        String url = "/clients/all";

        ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientListDto clientResponse = response.getBody();
        assertTrue(clientResponse.getClients().size() == 1);
        assertTrue(clientResponse.getClients().get(0).getEmail().equals(CLIENT_EMAIL));

        String urlToDelete = "/clients/delete/" + CLIENT_EMAIL + "/";

        client.delete(urlToDelete);

        response = client.getForEntity(url, ClientListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        clientResponse = response.getBody();
        assertTrue(clientResponse.getClients().size() == 0);
    }

    @Test
    public void testCreateClient() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertEquals(createdClient.getEmail(), CLIENT_EMAIL);
        assertEquals(createdClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(createdClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(createdClient.getAccountId());
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
    }

    @Test
    public void testCreateClient2() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create/", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertEquals(createdClient.getEmail(), CLIENT_EMAIL);
        assertEquals(createdClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(createdClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(createdClient.getAccountId());
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
    }

    @Test
    public void testUpdateClientFirstName() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/clients/updateFirstName/" + CLIENT_EMAIL + "/" + newFirstName;

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), newFirstName);
        assertEquals(updatedClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getFirstName(), newFirstName);
        assertTrue(clientRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateClientFirstName2() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/clients/updateFirstName/" + CLIENT_EMAIL + "/" + newFirstName + "/";

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), newFirstName);
        assertEquals(updatedClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getFirstName(), newFirstName);
        assertTrue(clientRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateClientLastName() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/clients/updateLastName/" + CLIENT_EMAIL + "/" + newLastName;

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(updatedClient.getLastName(), newLastName);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getLastName(), newLastName);
        assertTrue(clientRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateClientLastName2() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/clients/updateLastName/" + CLIENT_EMAIL + "/" + newLastName + "/";

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(updatedClient.getLastName(), newLastName);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getLastName(), newLastName);
        assertTrue(clientRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateClientPassword() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/clients/updatePassword/" + CLIENT_EMAIL + "/" + CLIENT_PASSWORD + "/" + newPassword;

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(updatedClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getPassword(), newPassword);
        assertTrue(clientRepository.findAll().size() == 1);
    }

    @Test
    public void testUpdateClientPassword2() {
        ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                CLIENT_PASSWORD);

        ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                ClientResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClientResponseDto createdClient = response.getBody();
        assertNotNull(createdClient);
        assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
        CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/clients/updatePassword/" + CLIENT_EMAIL + "/" + CLIENT_PASSWORD + "/" + newPassword + "/";

        ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                ClientResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        ClientResponseDto updatedClient = responseAfterUpdate.getBody();
        assertNotNull(updatedClient);
        assertEquals(updatedClient.getEmail(), CLIENT_EMAIL);
        assertEquals(updatedClient.getFirstName(), CLIENT_FISTNAME);
        assertEquals(updatedClient.getLastName(), CLIENT_LASTNAME);
        assertNotNull(updatedClient.getAccountId());
        assertTrue(updatedClient.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(clientRepository.findByEmail(CLIENT_EMAIL));
        assertEquals(clientRepository.findByEmail(CLIENT_EMAIL).getPassword(), newPassword);
        assertTrue(clientRepository.findAll().size() == 1);
    }

}
