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

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.LoginRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClientListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClientResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Client;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClientIntegrationTests {

        @Autowired
        private TestRestTemplate client;

        @Autowired
        private RegistrationRepository registrationRepository;

        @Autowired
        private OwnerRepository ownerRepository;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private InstructorRepository instructorRepository;

        @Autowired
        private ClassTypeRepository classTypeRepository;

        @Autowired
        private SpecificClassRepository specificClassRepository;

        @Autowired
        private LoginRepository loginRepository;

        @Autowired
        private PaymentMethodRepository paymentMethodRepository;

        @BeforeEach
        @AfterEach
        public void clearDatabase() {
                loginRepository.deleteAll();
                registrationRepository.deleteAll();
                specificClassRepository.deleteAll();
                classTypeRepository.deleteAll();
                instructorRepository.deleteAll();
                paymentMethodRepository.deleteAll();
                clientRepository.deleteAll();
                ownerRepository.deleteAll();
        }

        // Initialize variables
        private static final String CLIENT_EMAIL = "example@email.com";
        private static final String CLIENT_FISTNAME = "John";
        private static final String CLIENT_LASTNAME = "Doe";
        private static final String CLIENT_PASSWORD = "Password123";

        private int CLIENT_VALID_ACCOUNTID;

        @Test
        public void testFindClientByEmail() {

                // Create a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);

                // Save to the DB
                clientRepository.save(newClient);

                // Set the url
                String url = "/clients/getByEmail/" + String.valueOf(CLIENT_EMAIL);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

                // Validate the response
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
                // Create a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);

                // Save to the DB
                clientRepository.save(newClient);

                // Set the url
                String url = "/clients/getByEmail/" + String.valueOf(CLIENT_EMAIL) + "/";

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

                // Validate the response
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
                // Create & save a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(newClient);

                // Find the client with ID
                int validId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

                // Set the URL
                String url = "/clients/getById/" + String.valueOf(validId);

                // Get the reponse
                ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

                // Validate the reponse
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

                // Create & save a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(newClient);

                // Fint the client from the db with ID
                int validId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

                // Set the url
                String url = "/clients/getById/" + String.valueOf(validId) + "/";

                // Get the reponse
                ResponseEntity<ClientResponseDto> response = client.getForEntity(url, ClientResponseDto.class);

                // Validate the reponse
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

                // Create 2 new clients & save to the db
                Client client1 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                Client client2 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(client1);
                clientRepository.save(client2);

                // Set the url
                String url = "/clients/all";

                // Get the response
                ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

                // Validate the reponse
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

                // Create 2 new clients & save to the db
                Client client1 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                Client client2 = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(client1);
                clientRepository.save(client2);

                // Set the url
                String url = "/clients/all/";

                // Get the response
                ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

                // Validate the reponse
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

                // Create & save a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(newClient);

                // Set the url
                String url = "/clients/all";

                // Get the response
                ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                ClientListDto clientResponse = response.getBody();
                assertTrue(clientResponse.getClients().size() == 1);
                assertTrue(clientResponse.getClients().get(0).getEmail().equals(CLIENT_EMAIL));

                // Set the url
                String urlToDelete = "/clients/delete/" + CLIENT_EMAIL;

                // Delete the client
                client.delete(urlToDelete);

                // Get the response
                response = client.getForEntity(url, ClientListDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                clientResponse = response.getBody();
                assertTrue(clientResponse.getClients().size() == 0);
        }

        @Test
        public void testDeleteClientByEmail2() {

                // Create & save a new client
                Client newClient = new Client(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_PASSWORD, CLIENT_LASTNAME,
                                CLIENT_VALID_ACCOUNTID);
                clientRepository.save(newClient);

                // Set the url
                String url = "/clients/all";

                // Get the reponse
                ResponseEntity<ClientListDto> response = client.getForEntity(url, ClientListDto.class);

                // Validate the reponse
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                ClientListDto clientResponse = response.getBody();
                assertTrue(clientResponse.getClients().size() == 1);
                assertTrue(clientResponse.getClients().get(0).getEmail().equals(CLIENT_EMAIL));

                // Set the url
                String urlToDelete = "/clients/delete/" + CLIENT_EMAIL + "/";

                // Delete the client
                client.delete(urlToDelete);

                // Get the reponse
                response = client.getForEntity(url, ClientListDto.class);

                // Validate the reponse
                assertNotNull(response);
                assertEquals(HttpStatus.OK, response.getStatusCode());
                clientResponse = response.getBody();
                assertTrue(clientResponse.getClients().size() == 0);
        }

        @Test
        public void testCreateClient() {

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create/", request,
                                ClientResponseDto.class);

                // Validate the response
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

                // get the request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Creata new Name
                String newFirstName = "JohnTheNew";

                // Set the url
                String url = "/clients/updateFirstName/" + CLIENT_EMAIL + "/" + newFirstName;

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the reponse
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Create new name
                String newFirstName = "JohnTheNew";

                // Set the url
                String url = "/clients/updateFirstName/" + CLIENT_EMAIL + "/" + newFirstName + "/";

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Set a new last name
                String newLastName = "DoeTheNew";

                // Set the url
                String url = "/clients/updateLastName/" + CLIENT_EMAIL + "/" + newLastName;

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Create a new last name
                String newLastName = "DoeTheNew";

                // Set the url
                String url = "/clients/updateLastName/" + CLIENT_EMAIL + "/" + newLastName + "/";

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Set a new password
                String newPassword = "TheNewPass456";

                // Set the url
                String url = "/clients/updatePassword/" + CLIENT_EMAIL + "/" + CLIENT_PASSWORD + "/" + newPassword;

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the response
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

                // Create a request
                ClientRequestDto request = new ClientRequestDto(CLIENT_EMAIL, CLIENT_FISTNAME, CLIENT_LASTNAME,
                                CLIENT_PASSWORD);

                // Get the response
                ResponseEntity<ClientResponseDto> response = client.postForEntity("/clients/create", request,
                                ClientResponseDto.class);

                // Validate the response
                assertNotNull(response);
                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                ClientResponseDto createdClient = response.getBody();
                assertNotNull(createdClient);
                assertTrue(createdClient.getAccountId() > 0, "Response should have a positive ID.");
                CLIENT_VALID_ACCOUNTID = createdClient.getAccountId();

                // Create a new password
                String newPassword = "TheNewPass456";

                // Set the url
                String url = "/clients/updatePassword/" + CLIENT_EMAIL + "/" + CLIENT_PASSWORD + "/" + newPassword
                                + "/";

                // Get the response
                ResponseEntity<ClientResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null,
                                ClientResponseDto.class);

                // Validate the reponse
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
